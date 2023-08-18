package com.digiteo.neovoteIV.web.controller;

import com.digiteo.neovoteIV.model.jpa.data.AdminEntity;
import com.digiteo.neovoteIV.model.jpa.data.Election;
import com.digiteo.neovoteIV.model.jpa.data.ElectionStatus;
import com.digiteo.neovoteIV.model.jpa.data.Proposal;
import com.digiteo.neovoteIV.model.mapper.ElectionMapper;
import com.digiteo.neovoteIV.model.service.*;
import com.digiteo.neovoteIV.system.exception.ElectionAlreadyExistException;
import com.digiteo.neovoteIV.system.exception.ProposalNameAlreadyExistException;
import com.digiteo.neovoteIV.web.data.model.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@RequestMapping("/auth")
@Controller
@AllArgsConstructor
public class ElectionController {

    private ElectionService electionService;
    private ProposalService proposalService;
    private VoteService voteService;
    private ElectionMapper electionMapper;
    private FileService fileService;
    private AdminService adminService;

    @GetMapping("/election-new")
    public String newElectionView(final Model model, Principal principal){
        AdminEntity ae = adminService.getUserByUsernameOrEmail(principal.getName());
        AdminData data = new AdminData();
        data.setProfileImagePath(ae.getProfileImagePath());

        model.addAttribute("adminData", data);
        model.addAttribute("electionData", new ElectionData());
        return "auth/election-new";
    }

    // (!) check if there's any enhancement in using @AuthenticationPrincipal in the 'principal' parameter
    @PostMapping("/election-new")
    public String createNewElection(
            final @Valid ElectionData electionData,
            final BindingResult bindingResult,
            final Model model,
            Principal principal) {
        if(bindingResult.hasErrors()){
            model.addAttribute("electionForm", electionData);
            return "auth/election-new"; // check if it should be "auth/election-new?error" to display the constraint message
        }
        try{
            electionService.addElection(electionData, principal);
        }catch(ElectionAlreadyExistException e){
            bindingResult.rejectValue(
                    "title",
                    "electionData.title",
                    "Ya existe una elección con el título ingresado, por favor escoga otro.");
            model.addAttribute("electionForm", electionData);
            return "auth/election-new";
        }
        return "redirect:/auth/election-new?success";
    }

    @GetMapping("/elections-list")
    public String listElectionView(final Model model, Principal principal){
        AdminEntity ae = adminService.getUserByUsernameOrEmail(principal.getName());
        AdminData data = new AdminData();
        data.setProfileImagePath(ae.getProfileImagePath());

        List<ElectionListData> eld = electionService.getElectionsList();

        model.addAttribute("adminData", data);
        model.addAttribute("electionsList", eld);
        return "auth/elections-list";
    }

    @GetMapping("/election-edit")
    public String editElectionView(@RequestParam(value = "uid", required = false) String title,final Model model, Principal principal) {
        AdminEntity ae = adminService.getUserByUsernameOrEmail(principal.getName());
        AdminData data = new AdminData();
        data.setProfileImagePath(ae.getProfileImagePath());

        Election e = electionService.getElectionByTitle(title);
        ElectionData ed = electionMapper.toDto(e);
        boolean hasBegan = false;
        List<String> visibleProposals = new ArrayList<>();
        List<Proposal> proposals = e.getProposals();
        List<ProposalData> proposalsData = new ArrayList<>();
        for(Proposal p : proposals){
            ProposalData pd = new ProposalData();
            pd.setName(p.getName());
            if(p.getProfileImagePath() != null){
                pd.setProfileImagePath(p.getProfileImagePath());
            } else {
                pd.setProfileImagePath(null);
            }
            pd.setContactEmail(p.getContactEmail());
            pd.setDetails(p.getDetails());
            pd.setVisible(p.isVisible());
            if(pd.isVisible()){
                visibleProposals.add(p.getName());
            }
            pd.setCreatedAt(p.getTimestamp());
            proposalsData.add(pd);
        }

        if(LocalDateTime.now().compareTo(ed.getInitTimestamp()) == 0 ||
                LocalDateTime.now().compareTo(ed.getInitTimestamp()) > 0){
            hasBegan = true;
        }

        model.addAttribute("adminData", data);
        model.addAttribute("visibleProposals", visibleProposals);
        model.addAttribute("hasBegan", hasBegan);
        model.addAttribute("currentElection", ed);
        model.addAttribute("editableElection", new ElectionUpdateData());
        model.addAttribute("editableProposal", new ProposalUpdateData());
        model.addAttribute("proposalData", new ProposalData());
        model.addAttribute("proposalsList", proposalsData);
        return "auth/election-edit";
    }

    //------------------------------------------------------------------------------------------------------------------
    @PostMapping("/election-edit")
    public String addNewProposal(
            final @Valid @ModelAttribute("proposalData")ProposalData proposalData,
            final BindingResult bindingResult,
            @RequestParam(value = "uid", required = false) String electionTitle,
            final Model model,
            final RedirectAttributes redirectAttr) {
        Election e = electionService.getElectionByTitle(electionTitle);
        if(bindingResult.hasErrors()){
            ElectionData ed = new ElectionData(); // CHANGE THE ElectionData FOR A DTO WITH THE proposal LIST
            BeanUtils.copyProperties(e, ed);
            boolean flag = false;
            boolean hasBegan = false;
            List<Proposal> proposals = e.getProposals();
            List<ProposalData> proposalsData = new ArrayList<>();
            for(Proposal p : proposals){
                ProposalData pd = new ProposalData();
                pd.setName(p.getName());
                if(p.getProfileImagePath() != null){
                    pd.setProfileImagePath(p.getProfileImagePath());
                } else {
                    pd.setProfileImagePath(null);
                }
                pd.setContactEmail(p.getContactEmail());
                pd.setDetails(p.getDetails());
                pd.setVisible(p.isVisible());
                if(pd.isVisible()){
                    flag = true;
                }
                pd.setCreatedAt(p.getTimestamp());
                proposalsData.add(pd);
            }

            if(LocalDateTime.now().compareTo(ed.getInitTimestamp()) == 0 ||
                    LocalDateTime.now().compareTo(ed.getInitTimestamp()) > 0){
                hasBegan = true;
            }

            model.addAttribute("hasBegan", hasBegan);
            model.addAttribute("checkboxFlag", flag);
            model.addAttribute("currentElection", ed);
            model.addAttribute("editableElection", new ElectionUpdateData());
            model.addAttribute("editableProposal", new ProposalUpdateData());
            model.addAttribute("proposalsList", proposalsData);

            model.addAttribute("proposalRegistrationValidationErrors", true);
            //check if return a brand new ProposalData solves the SQL Error generated when a wrong field in sent 2 times
            model.addAttribute("proposalData", proposalData);
            return "auth/election-edit";
        }
        try{
            proposalService.addProposal(proposalData, e);
        }catch(ProposalNameAlreadyExistException ex){
            bindingResult.rejectValue(
                    "name",
                    "proposalData.name",
                    "Ya hay una propuesta con este nombre en el sistema, por favor eliga otro.");

            ElectionData ed = new ElectionData(); // CHANGE THE ElectionData FOR A DTO WITH THE proposal LIST
            BeanUtils.copyProperties(e, ed);
            boolean flag = false;
            boolean hasBegan = false;
            List<Proposal> proposals = e.getProposals();
            List<ProposalData> proposalsData = new ArrayList<>();
            for(Proposal p : proposals){
                ProposalData pd = new ProposalData();
                pd.setName(p.getName());
                if(p.getProfileImagePath() != null){
                    pd.setProfileImagePath(p.getProfileImagePath());
                } else {
                    pd.setProfileImagePath(null);
                }
                pd.setContactEmail(p.getContactEmail());
                pd.setDetails(p.getDetails());
                pd.setVisible(p.isVisible());
                if(pd.isVisible()){
                    flag = true;
                }
                pd.setCreatedAt(p.getTimestamp());
                proposalsData.add(pd);
            }

            if(LocalDateTime.now().compareTo(ed.getInitTimestamp()) == 0 ||
                    LocalDateTime.now().compareTo(ed.getInitTimestamp()) > 0){
                hasBegan = true;
            }

            model.addAttribute("hasBegan", hasBegan);
            model.addAttribute("checkboxFlag", flag);
            model.addAttribute("currentElection", ed);
            model.addAttribute("editableElection", new ElectionUpdateData());
            model.addAttribute("editableProposal", new ProposalUpdateData());
            model.addAttribute("proposalsList", proposalsData);
            model.addAttribute("proposalRegistrationValidationErrors", true);
            model.addAttribute("proposalData", proposalData);
            return "auth/election-edit";
        }
        redirectAttr.addAttribute("uid", electionTitle);
        return "redirect:/auth/election-edit";
    }

    //------------------------------------------------------------------------------------------------------------------
    @RequestMapping(value = "/election-edit", method = RequestMethod.DELETE, params = {"pid", "uid"})
    public String removeProposal(@RequestParam(value = "pid", required = false) String name,
                                 @RequestParam(value = "uid", required = false) String electionTitle,
                                 Model model){
        proposalService.deleteProposal(name);
        //check if the code bellow can be replaced with an invocation to the electionListView endpoint right here <-
        Election e = electionService.getElectionByTitle(electionTitle);
        ElectionData ed = new ElectionData(); // CHANGE THE ElectionData FOR A DTO WITH THE proposal LIST
        BeanUtils.copyProperties(e, ed);
        boolean flag = false;
        boolean hasBegan = false;
        List<Proposal> proposals = e.getProposals();
        List<ProposalData> proposalsData = new ArrayList<>();
        for(Proposal p : proposals){
            ProposalData pd = new ProposalData();
            pd.setName(p.getName());
            if(p.getProfileImagePath() != null){
                pd.setProfileImagePath(p.getProfileImagePath());
            } else {
                pd.setProfileImagePath(null);
            }
            pd.setContactEmail(p.getContactEmail());
            pd.setDetails(p.getDetails());
            pd.setVisible(p.isVisible());
            if(pd.isVisible()){
                flag = true;
            }
            pd.setCreatedAt(p.getTimestamp());
            proposalsData.add(pd);
        }

        if(LocalDateTime.now().compareTo(ed.getInitTimestamp()) == 0 ||
                LocalDateTime.now().compareTo(ed.getInitTimestamp()) > 0){
            hasBegan = true;
        }

        model.addAttribute("hasBegan", hasBegan);
        model.addAttribute("checkboxFlag", flag);
        model.addAttribute("currentElection", ed);
        model.addAttribute("editableElection", new ElectionUpdateData());
        model.addAttribute("editableProposal", new ProposalUpdateData());
        model.addAttribute("proposalData", new ProposalData());
        model.addAttribute("proposalsList", proposalsData);
        return "auth/election-edit";
    }

    //------------------------------------------------------------------------------------------------------------------
    @RequestMapping(value = "/election-edit/remove", method = RequestMethod.DELETE, params = {"uid"})
    public String removeElection(@RequestParam(value = "uid", required = false) String electionTitle){
        Election e = electionService.getElectionByTitle(electionTitle);
        electionService.deleteElection(e.getId());
        return "redirect:/auth/elections-list?success";
    }

    //------------------------------------------------------------------------------------------------------------------
    @PatchMapping(value = "/election-edit", params = {"uid"})
    public String editElectionPlusMax(final @Valid @ModelAttribute("editableElection") ElectionUpdateData electionUpdateData,
                                      final BindingResult bindingResult,
                                      @RequestParam(value = "uid", required = false) String electionTitle,
                                      final Model model) throws ElectionAlreadyExistException {

        Election e = electionService.getElectionByTitle(electionTitle);
        if(bindingResult.hasErrors()){
            ElectionData ed = new ElectionData(); // CHANGE THE ElectionData FOR A DTO WITH THE proposal LIST
            BeanUtils.copyProperties(e, ed);
            List<Proposal> proposals = e.getProposals();
            List<ProposalData> proposalsData = new ArrayList<>();
            for(Proposal p : proposals){
                ProposalData pd = new ProposalData();
                pd.setName(p.getName());
                if(p.getProfileImagePath() != null){
                    pd.setProfileImagePath(p.getProfileImagePath());
                } else {
                    pd.setProfileImagePath(null);
                }
                pd.setContactEmail(p.getContactEmail());
                pd.setDetails(p.getDetails());
                pd.setVisible(p.isVisible());
                pd.setCreatedAt(p.getTimestamp());
                proposalsData.add(pd);
            }
            model.addAttribute("currentElection", ed);
            model.addAttribute("editableElection", electionUpdateData);
            model.addAttribute("editableProposal", new ProposalUpdateData());
            model.addAttribute("proposalsList", proposalsData);

            model.addAttribute("electionEditValidationErrors", true);
            model.addAttribute("proposalData", new ProposalData());
            return "auth/election-edit";
        }
        try{
            electionService.checkIfElectionExist(electionUpdateData.getEditableTitle());
        } catch (ElectionAlreadyExistException ex){
            bindingResult.rejectValue(
                    "editableTitle",
                    "editableElection.editableTitle",
                    "Ya existe una elección con este título en el sistema, por favor eliga otro.");
            ElectionData ed = new ElectionData();
            BeanUtils.copyProperties(e, ed);
            List<Proposal> proposals = e.getProposals();
            List<ProposalData> proposalsData = new ArrayList<>();
            for(Proposal p : proposals){
                ProposalData pd = new ProposalData();
                pd.setName(p.getName());
                if(p.getProfileImagePath() != null){
                    pd.setProfileImagePath(p.getProfileImagePath());
                } else {
                    pd.setProfileImagePath(null);
                }
                pd.setContactEmail(p.getContactEmail());
                pd.setDetails(p.getDetails());
                pd.setVisible(p.isVisible());
                pd.setCreatedAt(p.getTimestamp());
                proposalsData.add(pd);
            }
            model.addAttribute("currentElection", ed);
            model.addAttribute("editableElection", electionUpdateData);
            model.addAttribute("editableProposal", new ProposalUpdateData());
            model.addAttribute("proposalsList", proposalsData);

            model.addAttribute("electionEditValidationErrors", true);
            model.addAttribute("proposalData", new ProposalData());
            return "auth/election-edit";
        }

        Election newElection = electionService.partialUpdatePlus(e.getId(), electionUpdateData);
        return "redirect:/auth/elections-list";
    }

    //------------------------------------------------------------------------------------------------------------------
    @PatchMapping(value = "/election-edit/suspend", params = {"uid"})
    public String suspendElection(@RequestParam(value = "uid", required = false) String electionTitle,
                                  final RedirectAttributes redirectAttr) {
        Election e = electionService.getElectionByTitle(electionTitle);
        if(e.getElectionStatus() == ElectionStatus.SUSPENDED){
            electionService.activateElection(e);
            redirectAttr.addAttribute("uid", electionTitle);
            return "redirect:/auth/election-edit";
        } else {
            electionService.suspendElection(e);
            return "redirect:/auth/elections-list";
        }
    }

    //------------------------------------------------------------------------------------------------------------------
    @RequestMapping(value = "/election-edit", method = RequestMethod.PATCH, params = {"pid", "uid"})
    public String editProposal(final @Valid @ModelAttribute("editableProposal")ProposalUpdateData proposalUpdateData,
                               final BindingResult bindingResult,
                               @RequestParam(value = "pid", required = false) String proposalName,
                               @RequestParam(value = "uid", required = false) String electionTitle,
                               final Model model,
                               final RedirectAttributes redirectAttr){
        Election e = electionService.getElectionByTitle(electionTitle);
        if(bindingResult.hasErrors()){
            ElectionData ed = new ElectionData(); // CHANGE THE ElectionData FOR A DTO WITH THE proposal LIST
            BeanUtils.copyProperties(e, ed);
            boolean flag = false;
            boolean hasBegan = false;
            List<Proposal> proposals = e.getProposals();
            List<ProposalData> proposalsData = new ArrayList<>();
            for(Proposal p : proposals){
                ProposalData pd = new ProposalData();
                pd.setName(p.getName());
                if(p.getProfileImagePath() != null){
                    pd.setProfileImagePath(p.getProfileImagePath());
                } else {
                    pd.setProfileImagePath(null);
                }
                pd.setContactEmail(p.getContactEmail());
                pd.setDetails(p.getDetails());
                pd.setVisible(p.isVisible());
                if(pd.isVisible()){
                    flag = true;
                }
                pd.setCreatedAt(p.getTimestamp());
                proposalsData.add(pd);
            }

            if(LocalDateTime.now().compareTo(ed.getInitTimestamp()) == 0 ||
                    LocalDateTime.now().compareTo(ed.getInitTimestamp()) > 0){
                hasBegan = true;
            }

            model.addAttribute("hasBegan", hasBegan);
            model.addAttribute("checkboxFlag", flag);
            model.addAttribute("currentElection", ed);
            model.addAttribute("editableElection", new ElectionUpdateData());
            model.addAttribute("editableProposal", proposalUpdateData);
            model.addAttribute("proposalsList", proposalsData);

            model.addAttribute("proposalEditValidationErrors", true);
            model.addAttribute("badEditProposalName", proposalName);
            model.addAttribute("proposalData", new ProposalData());
            return "auth/election-edit";
        }
        try{
            proposalService.checkIfProposalExist(proposalUpdateData.getName());
        } catch(ProposalNameAlreadyExistException ex){
            bindingResult.rejectValue(
                    "name",
                    "editableProposal.name",
                    "¡Ya hay una propuesta con este nombre!, por favor eliga otro.");
            ElectionData ed = new ElectionData();
            BeanUtils.copyProperties(e, ed);
            boolean flag = false;
            boolean hasBegan = false;
            List<Proposal> proposals = e.getProposals();
            List<ProposalData> proposalsData = new ArrayList<>();
            for(Proposal p : proposals){
                ProposalData pd = new ProposalData();
                pd.setName(p.getName());
                if(p.getProfileImagePath() != null){
                    pd.setProfileImagePath(p.getProfileImagePath());
                } else {
                    pd.setProfileImagePath(null);
                }
                pd.setContactEmail(p.getContactEmail());
                pd.setDetails(p.getDetails());
                pd.setVisible(p.isVisible());
                if(pd.isVisible()){
                    flag = true;
                }
                pd.setCreatedAt(p.getTimestamp());
                proposalsData.add(pd);
            }

            if(LocalDateTime.now().compareTo(ed.getInitTimestamp()) == 0 ||
                    LocalDateTime.now().compareTo(ed.getInitTimestamp()) > 0){
                hasBegan = true;
            }

            model.addAttribute("hasBegan", hasBegan);
            model.addAttribute("checkboxFlag", flag);
            model.addAttribute("currentElection", ed);
            model.addAttribute("editableElection", new ElectionUpdateData());
            model.addAttribute("editableProposal", proposalUpdateData);
            model.addAttribute("proposalsList", proposalsData);

            model.addAttribute("proposalEditValidationErrors", true);
            model.addAttribute("proposalData", new ProposalData());
            return "auth/election-edit";
        }
        proposalService.partialUpdatePlus(proposalName, proposalUpdateData);

        redirectAttr.addAttribute("uid", electionTitle);
        return "redirect:/auth/election-edit";
    }

    //------------------------------------------------------------------------------------------------------------------
    @GetMapping(value = "/election-results")
    public String electionResultsView(@RequestParam(value = "uid", required = false) String title, final Model model, Principal principal){
        AdminEntity ae = adminService.getUserByUsernameOrEmail(principal.getName());
        AdminData data = new AdminData();
        data.setProfileImagePath(ae.getProfileImagePath());

        Election e = electionService.getElectionByTitle(title);
        int totalVoters = e.getRoll().size();
        int totalVotes = 0;
        double participationPercent = 0;
        ElectionData ed = electionMapper.toDto(e);
        boolean isVisible = false;
        List<Proposal> proposals = e.getProposals();
        List<ProposalResultData> proposalsResultsList = new ArrayList<>();
        String[] mostVoted = voteService.findMostVoted(proposals);
        Comparator<ProposalResultData> comparator
                = Comparator.comparing(ProposalResultData::getVotes);

        model.addAttribute("adminData", data);
        model.addAttribute("currentElection", ed);

        if(mostVoted.length > 1){
            if(mostVoted.length == 2){
                for(Proposal p:proposals){
                    ProposalResultData prd = new ProposalResultData();
                    prd.setName(p.getName());
                    if(p.getProfileImagePath() != null){
                        prd.setProfileImagePath(p.getProfileImagePath());
                    } else {
                        prd.setProfileImagePath(null);
                    }
                    prd.setEmail(p.getContactEmail());
                    prd.setVotes(p.getVotes().size());
                    if(totalVoters > 0){
                        prd.setPercent(prd.getVotes()*100/totalVoters); //limit to 2 the digits or cast to Integer
                    } else {
                        prd.setPercent(0);
                    }
                    prd.setCreatedAt(p.getTimestamp());
                    prd.setVisible(p.isVisible());
                    if(prd.isVisible() && prd.getName() != mostVoted[0] && prd.getName() != mostVoted[1]){
                        isVisible = true;
                        proposalsResultsList.add(prd); // order the adding in function of the number of votes or percent
                        totalVotes = totalVotes + prd.getVotes();
                    }
                }
                Proposal p1 = proposalService.findProposal(mostVoted[0]);
                ProposalResultData tieProposalOne = new ProposalResultData();
                tieProposalOne.setName(p1.getName());
                if(p1.getProfileImagePath() != null){
                    tieProposalOne.setProfileImagePath(p1.getProfileImagePath());
                } else {
                    tieProposalOne.setProfileImagePath(null);
                }
                tieProposalOne.setVotes(p1.getVotes().size());
                if(totalVoters > 0){
                    tieProposalOne.setPercent(tieProposalOne.getVotes()*100/totalVoters); //limit to 2 the digits or cast to Integer
                } else {
                    tieProposalOne.setPercent(0);
                }
                tieProposalOne.setCreatedAt(p1.getTimestamp());

                Proposal p2 = proposalService.findProposal(mostVoted[1]);
                ProposalResultData tieProposalTwo = new ProposalResultData();
                tieProposalTwo.setName(p2.getName());
                if(p2.getProfileImagePath() != null){
                    tieProposalTwo.setProfileImagePath(p2.getProfileImagePath());
                } else {
                    tieProposalTwo.setProfileImagePath(null);
                }
                tieProposalTwo.setVotes(p2.getVotes().size());
                if(totalVoters > 0){
                    tieProposalTwo.setPercent(tieProposalOne.getVotes()*100/totalVoters); //limit to 2 the digits or cast to Integer
                } else {
                    tieProposalTwo.setPercent(0);
                }
                tieProposalTwo.setCreatedAt(p2.getTimestamp());

                totalVotes = totalVotes + tieProposalOne.getVotes() + tieProposalTwo.getVotes();
                if(totalVoters > 0){
                    participationPercent = totalVotes*100/totalVoters; //limit to 2 the digits or cast to Integer
                } else {
                    participationPercent = 0;
                }

                Collections.sort(proposalsResultsList, comparator);
                Collections.reverse(proposalsResultsList);
                int[] rankingIndexes = new int[proposalsResultsList.size()];

                for(ProposalResultData prd:proposalsResultsList){
                    int index = proposalsResultsList.indexOf(prd);
                    rankingIndexes[index] = index + 3;
                }

                model.addAttribute("rankingIndexes", rankingIndexes);
                model.addAttribute("tieProposalOne", tieProposalOne);
                model.addAttribute("tieProposalTwo", tieProposalTwo);
                model.addAttribute("totalVotes", totalVotes);
                model.addAttribute("participationPercent", participationPercent);
                model.addAttribute("otherProposals", proposalsResultsList);

                return "auth/election-results";
            } else {
                for(Proposal p:proposals){
                    ProposalResultData prd = new ProposalResultData();
                    prd.setName(p.getName());
                    if(p.getProfileImagePath() != null){
                        prd.setProfileImagePath(p.getProfileImagePath());
                    } else {
                        prd.setProfileImagePath(null);
                    }
                    prd.setEmail(p.getContactEmail());
                    prd.setVotes(p.getVotes().size());
                    if(totalVoters > 0){
                        prd.setPercent(prd.getVotes()*100/totalVoters); //limit to 2 the digits or cast to Integer
                    } else {
                        prd.setPercent(0);
                    }
                    prd.setCreatedAt(p.getTimestamp());
                    prd.setVisible(p.isVisible());
                    if(prd.isVisible()){
                        isVisible = true;
                        proposalsResultsList.add(prd); // order the adding in function of the number of votes or percent
                        totalVotes = totalVotes + prd.getVotes();
                    }
                }
                if(totalVoters > 0){
                    participationPercent = totalVotes*100/totalVoters;
                } else {
                    participationPercent = 0;
                }

                Collections.sort(proposalsResultsList, comparator);
                Collections.reverse(proposalsResultsList);
                int[] rankingIndexes = new int[proposalsResultsList.size()];

                for(ProposalResultData prd:proposalsResultsList){
                    int index = proposalsResultsList.indexOf(prd);
                    rankingIndexes[index] = index + 1;
                }

                model.addAttribute("rankingIndexes", rankingIndexes);
                model.addAttribute("totalVotes", totalVotes);
                model.addAttribute("participationPercent", participationPercent);
                model.addAttribute("otherProposals", proposalsResultsList);

                return "auth/election-results";
            }
        }

        for(Proposal p:proposals){
            ProposalResultData prd = new ProposalResultData();
            prd.setName(p.getName());
            if(p.getProfileImagePath() != null){
                prd.setProfileImagePath(p.getProfileImagePath());
            } else {
                prd.setProfileImagePath(null);
            }
            prd.setEmail(p.getContactEmail());
            prd.setVotes(p.getVotes().size());
            if(totalVoters > 0){
                prd.setPercent(prd.getVotes()*100/totalVoters);
            } else {
                prd.setPercent(0);
            }
            prd.setCreatedAt(p.getTimestamp());
            prd.setVisible(p.isVisible());
            if(prd.isVisible() && prd.getName() != mostVoted[0]){
                isVisible = true;
                proposalsResultsList.add(prd); // order the adding in function of the number of votes or percent
                totalVotes = totalVotes + prd.getVotes();
            }
        }
        Proposal p = proposalService.findProposal(mostVoted[0]);
        ProposalResultData winner = new ProposalResultData();
        winner.setName(p.getName());
        if(p.getProfileImagePath() != null){
            winner.setProfileImagePath(p.getProfileImagePath());
        } else {
            winner.setProfileImagePath(null);
        }
        winner.setVotes(p.getVotes().size());
        if(totalVoters > 0){
            winner.setPercent(winner.getVotes()*100/totalVoters); //limit to 2 the digits or cast to Integer
        } else {
            winner.setPercent(0);
        }
        winner.setCreatedAt(p.getTimestamp());

        totalVotes = totalVotes + winner.getVotes();
        if(totalVoters > 0){
            participationPercent = totalVotes*100/totalVoters;
        } else {
            participationPercent = 0;
        }

        Collections.sort(proposalsResultsList, comparator);
        Collections.reverse(proposalsResultsList);
        int[] rankingIndexes = new int[proposalsResultsList.size()];

        for(ProposalResultData prd:proposalsResultsList){
            int index = proposalsResultsList.indexOf(prd);
            rankingIndexes[index] = index + 2;
        }

        model.addAttribute("rankingIndexes", rankingIndexes);
        model.addAttribute("totalVotes", totalVotes);
        model.addAttribute("participationPercent", participationPercent);
        model.addAttribute("winnerProposal", winner);
        model.addAttribute("otherProposals", proposalsResultsList);

        return "auth/election-results";
    }
    //------------------------------------------------------------------------------------------------------------------
    @PostMapping("/election-edit-proposal-img")
    public String addProposalImage(@RequestParam(value = "pid", required = false) String proposalName,
                                   @RequestParam(value = "uid", required = false) String electionTitle,
                                   @RequestParam("file") MultipartFile file,
                                   final RedirectAttributes redirectAttr) throws IOException, InterruptedException {
        fileService.uploadFile(file, proposalName, null, null, null);
        //this delay is for the image to be completely transferred into the specified folder before render the view
        Thread.sleep(2000);
        redirectAttr.addAttribute("uid", electionTitle);
        return "redirect:/auth/election-edit";
    }

    //------------------------------------------------------------------------------------------------------------------
    @PostMapping("/election-edit-img")
    public String addElectionImage(@RequestParam(value = "uid", required = false) String electionTitle,
                                   @RequestParam("file") MultipartFile file,
                                   final RedirectAttributes redirectAttr) throws IOException, InterruptedException {
        fileService.uploadFile(file, null, electionTitle, null, null);
        Thread.sleep(2000);
        redirectAttr.addAttribute("uid", electionTitle);
        return "redirect:/auth/election-edit";
    }
}
