package com.digiteo.neovoteIV.web.controller;

import com.digiteo.neovoteIV.model.jpa.data.Election;
import com.digiteo.neovoteIV.model.jpa.data.Proposal;
import com.digiteo.neovoteIV.model.service.ElectionService;
import com.digiteo.neovoteIV.model.service.ProposalService;
import com.digiteo.neovoteIV.system.exception.ElectionAlreadyExistException;
import com.digiteo.neovoteIV.system.exception.ProposalNameAlreadyExistException;
import com.digiteo.neovoteIV.web.data.model.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.slf4j.ILoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RequestMapping("/auth")
@Controller
@AllArgsConstructor
public class ElectionController {

    private ElectionService electionService;
    private ProposalService proposalService;

    @GetMapping("/election-new")
    public String newElectionView(final Model model){
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
    public String listElectionView(final Model model){
        List<ElectionListData> eld = electionService.getElectionsList();
        model.addAttribute("electionsList", eld);
        return "auth/elections-list";
    }

    @GetMapping("/election-edit")
    public String editElectionView(@RequestParam(value = "uid", required = false) String title,final Model model){
        Election e = electionService.getElectionByTitle(title);
        ElectionData ed = new ElectionData(); // CHANGE THE ElectionData FOR A DTO WITH THE proposal LIST
        BeanUtils.copyProperties(e, ed);
        List<Proposal> proposals = e.getProposals();
        List<ProposalData> proposalsData = new ArrayList<>();
        for(Proposal p : proposals){
            ProposalData pd = new ProposalData();
            pd.setName(p.getName());
            pd.setContactEmail(p.getContactEmail());
            pd.setDetails(p.getDetails());
            pd.setVisible(p.isVisible());
            pd.setCreatedAt(p.getTimestamp());
            proposalsData.add(pd);
        }

        model.addAttribute("currentElection", ed);
        model.addAttribute("editableElection", new ElectionUpdateData());
        model.addAttribute("editableProposal", new ProposalUpdateData());
        model.addAttribute("proposalData", new ProposalData());
        model.addAttribute("proposalsList", proposalsData);
        return "auth/election-edit";
    }

    @PostMapping("/election-edit")
    public String addNewProposal(
            final @Valid @ModelAttribute("proposalData")ProposalData proposalData,
            final BindingResult bindingResult,
            @RequestParam(value = "uid", required = false) String electionTitle,
            final Model model) {
        Election e = electionService.getElectionByTitle(electionTitle);
        if(bindingResult.hasErrors()){
            //Election e = electionService.getElectionByTitle(electionTitle);
            ElectionData ed = new ElectionData(); // CHANGE THE ElectionData FOR A DTO WITH THE proposal LIST
            BeanUtils.copyProperties(e, ed);
            List<Proposal> proposals = e.getProposals();
            List<ProposalData> proposalsData = new ArrayList<>();
            for(Proposal p : proposals){
                ProposalData pd = new ProposalData();
                pd.setName(p.getName());
                pd.setContactEmail(p.getContactEmail());
                pd.setDetails(p.getDetails());
                pd.setVisible(p.isVisible());
                pd.setCreatedAt(p.getTimestamp());
                proposalsData.add(pd);
            }
            model.addAttribute("currentElection", ed);
            model.addAttribute("editableElection", new ElectionUpdateData());
            model.addAttribute("editableProposal", new ProposalUpdateData());
            model.addAttribute("proposalsList", proposalsData);

            model.addAttribute("proposalRegistrationValidationErrors", true);
            model.addAttribute("proposalData", proposalData);
            return "auth/election-edit"; // check if it should be "auth/election-new?error" to display the constraint message
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
            List<Proposal> proposals = e.getProposals();
            List<ProposalData> proposalsData = new ArrayList<>();
            for(Proposal p : proposals){
                ProposalData pd = new ProposalData();
                pd.setName(p.getName());
                pd.setContactEmail(p.getContactEmail());
                pd.setDetails(p.getDetails());
                pd.setVisible(p.isVisible());
                pd.setCreatedAt(p.getTimestamp());
                proposalsData.add(pd);
            }
            model.addAttribute("currentElection", ed);
            model.addAttribute("editableElection", new ElectionUpdateData());
            model.addAttribute("editableProposal", new ProposalUpdateData());
            model.addAttribute("proposalsList", proposalsData);
            model.addAttribute("proposalData", proposalData);
            return "auth/election-edit";
        }
        ElectionData ed = new ElectionData(); // CHANGE THE ElectionData FOR A DTO WITH THE proposal LIST
        BeanUtils.copyProperties(e, ed);
        List<Proposal> proposals = e.getProposals();
        List<ProposalData> proposalsData = new ArrayList<>();
        for(Proposal p : proposals){
            ProposalData pd = new ProposalData();
            pd.setName(p.getName());
            pd.setContactEmail(p.getContactEmail());
            pd.setDetails(p.getDetails());
            pd.setVisible(p.isVisible());
            pd.setCreatedAt(p.getTimestamp());
            proposalsData.add(pd);
        }
        model.addAttribute("currentElection", ed);
        model.addAttribute("editableElection", new ElectionUpdateData());
        model.addAttribute("editableProposal", new ProposalUpdateData());
        model.addAttribute("proposalsList", proposalsData);
        model.addAttribute("proposalData", proposalData);
        //return "redirect:/auth/election-edit?success"; //Check if this is the correct approach to announce the success
        return "auth/election-edit";
    }

    @RequestMapping(value = "/election-edit", method = RequestMethod.DELETE, params = {"pid", "uid"})
    public String removeProposal(@RequestParam(value = "pid", required = false) String name,
                                 @RequestParam(value = "uid", required = false) String electionTitle,
                                 Model model){
        proposalService.deleteProposal(name);
        Election e = electionService.getElectionByTitle(electionTitle);
        ElectionData ed = new ElectionData(); // CHANGE THE ElectionData FOR A DTO WITH THE proposal LIST
        BeanUtils.copyProperties(e, ed);
        List<Proposal> proposals = e.getProposals();
        List<ProposalData> proposalsData = new ArrayList<>();
        for(Proposal p : proposals){
            ProposalData pd = new ProposalData();
            pd.setName(p.getName());
            pd.setContactEmail(p.getContactEmail());
            pd.setDetails(p.getDetails());
            pd.setVisible(p.isVisible());
            pd.setCreatedAt(p.getTimestamp());
            proposalsData.add(pd);
        }
        model.addAttribute("currentElection", ed);
        model.addAttribute("editableElection", new ElectionUpdateData());
        model.addAttribute("editableProposal", new ProposalUpdateData());
        model.addAttribute("proposalData", new ProposalData());
        model.addAttribute("proposalsList", proposalsData);
        return "auth/election-edit";
    }

    @RequestMapping(value = "/election-edit/remove", method = RequestMethod.DELETE, params = {"uid"})
    public String removeElection(@RequestParam(value = "uid", required = false) String electionTitle){
        Election e = electionService.getElectionByTitle(electionTitle);
        electionService.deleteElection(e.getId());
        return "redirect:/auth/elections-list?success";
    }

    /*
    @RequestMapping(value = "/election-edit", method = RequestMethod.PATCH, params = {"uid"})
    public String editElection(@RequestParam(value = "uid", required = false) String electionTitle,
                               final @Valid @ModelAttribute("editableElection")ElectionUpdateData electionUpdateData,
                               final BindingResult bindingResult,
                               final Model model){
        Election e = electionService.getElectionByTitle(electionTitle);
        if(bindingResult.hasErrors()){
            ElectionData ed = new ElectionData(); // CHANGE THE ElectionData FOR A DTO WITH THE proposal LIST
            BeanUtils.copyProperties(e, ed);
            List<Proposal> proposals = e.getProposals();
            List<ProposalData> proposalsData = new ArrayList<>();
            for(Proposal p : proposals){
                ProposalData pd = new ProposalData();
                pd.setName(p.getName());
                pd.setContactEmail(p.getContactEmail());
                pd.setDetails(p.getDetails());
                pd.setVisible(p.isVisible());
                pd.setCreatedAt(p.getTimestamp());
                proposalsData.add(pd);
            }
            model.addAttribute("currentElection", ed);
            model.addAttribute("editableElection", new ElectionUpdateData());
            model.addAttribute("editableProposal", new ProposalUpdateData());
            model.addAttribute("proposalsList", proposalsData);

            model.addAttribute("electionEditValidationErrors", true);
            model.addAttribute("proposalData", new ProposalData());
            return "auth/election-edit"; // check if it should be "auth/election-new?error" to display the constraint message
        }
        try{
            String newTitle = electionService.partialUpdate(e.getId(), electionUpdateData);

            Election newElection = electionService.getElectionByTitle(newTitle);
            ElectionData ed = new ElectionData();
            BeanUtils.copyProperties(newElection, ed);
            List<Proposal> proposals = newElection.getProposals();
            List<ProposalData> proposalsData = new ArrayList<>();
            for(Proposal p : proposals){
                ProposalData pd = new ProposalData();
                pd.setName(p.getName());
                pd.setContactEmail(p.getContactEmail());
                pd.setDetails(p.getDetails());
                pd.setVisible(p.isVisible());
                pd.setCreatedAt(p.getTimestamp());
                proposalsData.add(pd);
            }
            model.addAttribute("currentElection", ed); // this must contain the values of the new updated election <<<
            model.addAttribute("editableElection", new ElectionUpdateData());
            model.addAttribute("editableProposal", new ProposalUpdateData());
            model.addAttribute("proposalData", new ProposalData());
            model.addAttribute("proposalsList", proposalsData);

            return "auth/election-edit";

        } catch(ElectionAlreadyExistException ex){
            bindingResult.rejectValue(
                    "title",
                    "editableElection.title",
                    "Ya existe una elección con este título registrada, por favor eliga otro.");

            ElectionData ed = new ElectionData(); // CHANGE THE ElectionData FOR A DTO WITH THE proposal LIST
            BeanUtils.copyProperties(e, ed);
            List<Proposal> proposals = e.getProposals();
            List<ProposalData> proposalsData = new ArrayList<>();
            for(Proposal p : proposals){
                ProposalData pd = new ProposalData();
                pd.setName(p.getName());
                pd.setContactEmail(p.getContactEmail());
                pd.setDetails(p.getDetails());
                pd.setVisible(p.isVisible());
                pd.setCreatedAt(p.getTimestamp());
                proposalsData.add(pd);
            }

            model.addAttribute("currentElection", ed);
            model.addAttribute("editableElection", new ElectionUpdateData());
            model.addAttribute("editableProposal", new ProposalUpdateData());
            model.addAttribute("proposalsList", proposalsData);

            model.addAttribute("electionEditValidationErrors", true);
            model.addAttribute("proposalData", new ProposalData());
            return "auth/election-edit";
        }
        //return "redirect:/auth/election-edit";
    }
    */

    //------------------------------------------------------------------------------------------------------------------

    /*
    @RequestMapping(value = "/election-edit/edit-e", method = RequestMethod.PATCH, params = {"uid"})
    public String editElectionPlus(final @Valid @ModelAttribute("editableElection")ElectionUpdateData electionUpdateData,
                               final BindingResult bindingResult,
                               @RequestParam(value = "uid", required = false) String electionTitle,
                               final Model model){
        Election newElection = electionService.partialUpdatePlus(electionService.getElectionByTitle(electionTitle).getId(),
                electionUpdateData);

        ElectionData ed = new ElectionData();
        BeanUtils.copyProperties(newElection, ed);
        List<Proposal> proposals = newElection.getProposals();
        List<ProposalData> proposalsData = new ArrayList<>();
        for(Proposal p : proposals){
            ProposalData pd = new ProposalData();
            pd.setName(p.getName());
            pd.setContactEmail(p.getContactEmail());
            pd.setDetails(p.getDetails());
            pd.setVisible(p.isVisible());
            pd.setCreatedAt(p.getTimestamp());
            proposalsData.add(pd);
        }
        model.addAttribute("currentElection", ed); // this must contain the values of the new updated election <<<
        model.addAttribute("editableElection", new ElectionUpdateData());
        model.addAttribute("editableProposal", new ProposalUpdateData());
        model.addAttribute("proposalData", new ProposalData());
        model.addAttribute("proposalsList", proposalsData);
        return "auth/election-edit";
    }
    */

    //------------------------------------------------------------------------------------------------------------------

    @PatchMapping(value = "/election-edit", params = {"uid"})
    public String editElectionPlusMax(final @Valid @ModelAttribute("editableElection") ElectionUpdateData electionUpdateData,
                                      BindingResult bindingResult,
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
                pd.setContactEmail(p.getContactEmail());
                pd.setDetails(p.getDetails());
                pd.setVisible(p.isVisible());
                pd.setCreatedAt(p.getTimestamp());
                proposalsData.add(pd);
            }
            model.addAttribute("currentElection", ed);
            model.addAttribute("editableElection", new ElectionUpdateData());
            model.addAttribute("editableProposal", new ProposalUpdateData());
            model.addAttribute("proposalsList", proposalsData);

            model.addAttribute("electionEditValidationErrors", true);
            model.addAttribute("proposalData", new ProposalData());
            return "auth/election-edit"; // check if it should be "auth/election-new?error" to display the constraint message
        }
        if (Objects.equals(electionTitle, electionUpdateData.getEditableTitle())) {
            throw new ElectionAlreadyExistException("Ya existe una elección con este título registrada, por favor eliga otro.");
        }
        Election newElection = electionService.partialUpdatePlus(e.getId(), electionUpdateData);

        ElectionData ed2 = new ElectionData();
        BeanUtils.copyProperties(newElection, ed2);
        List<Proposal> proposals = newElection.getProposals();
        List<ProposalData> proposalsData = new ArrayList<>();
        for (Proposal p : proposals) {
            ProposalData pd = new ProposalData();
            pd.setName(p.getName());
            pd.setContactEmail(p.getContactEmail());
            pd.setDetails(p.getDetails());
            pd.setVisible(p.isVisible());
            pd.setCreatedAt(p.getTimestamp());
            proposalsData.add(pd);
        }
        model.addAttribute("currentElection", ed2); // this must contain the values of the new updated election <<<
        model.addAttribute("editableElection", new ElectionUpdateData());
        model.addAttribute("editableProposal", new ProposalUpdateData());
        model.addAttribute("proposalData", new ProposalData());
        model.addAttribute("proposalsList", proposalsData);
        return "auth/election-edit";
    }

    //------------------------------------------------------------------------------------------------------------------


    @RequestMapping(value = "/election-edit/edit-p", method = RequestMethod.PATCH, params = {"pid", "uid"})
    public String editProposal(final @Valid @ModelAttribute("editableProposal")ProposalUpdateData proposalUpdateData,
                               final BindingResult bindingResult,
                               @RequestParam(value = "pid", required = false) String proposalName,
                               @RequestParam(value = "uid", required = false) String electionTitle,
                               final Model model){
        Election e = electionService.getElectionByTitle(electionTitle);
        if(bindingResult.hasErrors()){
            ElectionData ed = new ElectionData(); // CHANGE THE ElectionData FOR A DTO WITH THE proposal LIST
            BeanUtils.copyProperties(e, ed);
            List<Proposal> proposals = e.getProposals();
            List<ProposalData> proposalsData = new ArrayList<>();
            for(Proposal p : proposals){
                ProposalData pd = new ProposalData();
                pd.setName(p.getName());
                pd.setContactEmail(p.getContactEmail());
                pd.setDetails(p.getDetails());
                pd.setVisible(p.isVisible());
                pd.setCreatedAt(p.getTimestamp());
                proposalsData.add(pd);
            }
            model.addAttribute("currentElection", ed);
            model.addAttribute("editableElection", new ElectionUpdateData());
            model.addAttribute("editableProposal", proposalUpdateData); // maybe change this for a new one
            model.addAttribute("proposalsList", proposalsData);

            model.addAttribute("proposalEditValidationErrors", true);
            model.addAttribute("proposalData", new ProposalData());
            return "auth/election-edit"; // check if it should be "auth/election-new?error" to display the constraint message
        }
        try{
            proposalService.partialUpdate(proposalName, proposalUpdateData);
        } catch(ProposalNameAlreadyExistException ex){
            bindingResult.rejectValue(
                    "name",
                    "editableProposal.name",
                    "Ya hay una propuesta con ese nombre en el sistema, por favor eliga otro.");

            ElectionData ed = new ElectionData(); // CHANGE THE ElectionData FOR A DTO WITH THE proposal LIST
            BeanUtils.copyProperties(e, ed);
            List<Proposal> proposals = e.getProposals();
            List<ProposalData> proposalsData = new ArrayList<>();
            for(Proposal p : proposals){
                ProposalData pd = new ProposalData();
                pd.setName(p.getName());
                pd.setContactEmail(p.getContactEmail());
                pd.setDetails(p.getDetails());
                pd.setVisible(p.isVisible());
                pd.setCreatedAt(p.getTimestamp());
                proposalsData.add(pd);
            }
            model.addAttribute("currentElection", ed);
            model.addAttribute("editableElection", new ElectionUpdateData());
            model.addAttribute("editableProposal", proposalUpdateData);
            model.addAttribute("proposalsList", proposalsData);

            model.addAttribute("proposalEditValidationErrors", true);
            model.addAttribute("proposalData", new ProposalData());
            return "auth/election-edit";
        }
        ElectionData ed = new ElectionData();
        BeanUtils.copyProperties(e, ed);
        List<Proposal> proposals = e.getProposals();
        List<ProposalData> proposalsData = new ArrayList<>();
        for(Proposal p : proposals){
            ProposalData pd = new ProposalData();
            pd.setName(p.getName());
            pd.setContactEmail(p.getContactEmail());
            pd.setDetails(p.getDetails());
            pd.setVisible(p.isVisible());
            pd.setCreatedAt(p.getTimestamp());
            proposalsData.add(pd);
        }
        model.addAttribute("currentElection", ed); // this must contain the values of the new updated election <<<
        model.addAttribute("editableElection", new ElectionUpdateData());
        model.addAttribute("editableProposal", new ProposalUpdateData()); // CHANGE 1
        model.addAttribute("proposalData", new ProposalData());
        model.addAttribute("proposalsList", proposalsData);

        return "redirect:/auth/election-edit"; // CHANGE 2
    }
}
