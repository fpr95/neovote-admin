package com.digiteo.neovoteIV.web.controller;

import com.digiteo.neovoteIV.model.jpa.data.Election;
import com.digiteo.neovoteIV.model.jpa.data.Proposal;
import com.digiteo.neovoteIV.model.jpa.data.Vote;
import com.digiteo.neovoteIV.model.jpa.data.VoterEntity;
import com.digiteo.neovoteIV.model.mapper.ElectionMapper;
import com.digiteo.neovoteIV.model.service.ElectionService;
import com.digiteo.neovoteIV.model.service.ProposalService;
import com.digiteo.neovoteIV.model.service.VoteService;
import com.digiteo.neovoteIV.model.service.VoterService;
import com.digiteo.neovoteIV.web.data.model.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.*;

@RequestMapping("/auth-voter")
@Controller
@AllArgsConstructor
public class UserElectionController {

    private ElectionService electionService;
    private ElectionMapper electionMapper;
    private VoterService voterService;
    private ProposalService proposalService;
    private VoteService voteService;

    @GetMapping("/user-elections-list")
    public String electionsListView(final Model model, Principal principal){
        List<ElectionListData> eld = electionService.getVotersElectionsList(principal);
        VoterEntity v = voterService.getVoterByUsernameOrEmail(principal.getName());
        VoterData data = new VoterData();
        data.setProfileImagePath(v.getProfileImagePath());

        model.addAttribute("voterData", data);
        model.addAttribute("voterElectionsList", eld);
        return "auth-voter/user-elections-list";
    }

    @GetMapping("/user-election-details")
    public String electionDetailsView(@RequestParam(value = "uid", required = false) String title, final Model model, Principal principal) {
        VoterEntity v = voterService.getVoterByUsernameOrEmail(principal.getName());
        VoterData data = new VoterData();
        data.setProfileImagePath(v.getProfileImagePath());

        Election e = electionService.getElectionByTitle(title);
        ElectionData ed = electionMapper.toDto(e);
        boolean flag = false; // check if this flag is necessary, if not, take it out
        List<Proposal> proposals = e.getProposals(); // the fetch type for the proposals is LAZY
        List<ProposalData> proposalsData = new ArrayList<>();
        for(Proposal p : proposals) {
            ProposalData pd = new ProposalData();
            pd.setName(p.getName());
            if(p.getProfileImagePath() != null){
                pd.setProfileImagePath(p.getProfileImagePath());
            } else {
                pd.setProfileImagePath(null);
            }
            pd.setContactEmail(p.getContactEmail());
            //check if it's necessary a null check for the web and rrss fields
            pd.setWebId(p.getWebId());
            pd.setFacebookId(p.getFacebookId());
            pd.setLinkedinId(p.getLinkedinId());
            pd.setXId(p.getXId());
            pd.setInstagramId(p.getInstagramId());

            pd.setDetails(p.getDetails());
            pd.setVisible(p.isVisible());
            pd.setCreatedAt(p.getTimestamp());
            if(pd.isVisible()){
                flag = true;
                proposalsData.add(pd);
            }
        }
        model.addAttribute("voterData", data);
        model.addAttribute("checkboxFlag", flag); // check if this flag is necessary, if not, take it out
        model.addAttribute("currentElection", ed);
        model.addAttribute("proposalsList", proposalsData);

        return "auth-voter/user-election-details";
    }

    @GetMapping("/user-ballot-box")
    public String ballotBoxView(@RequestParam(value = "uid", required = false) String title, final Model model, Principal principal){
        VoterEntity ve = voterService.getVoterByUsernameOrEmail(principal.getName());
        VoterData data = new VoterData();
        data.setProfileImagePath(ve.getProfileImagePath());

        Election e = electionService.getElectionByTitle(title);
        ElectionData ed = electionMapper.toDto(e);
        boolean hasVoted = false;
        boolean flag = false;
        List<Proposal> proposals = e.getProposals();
        List<ProposalData> proposalsData = new ArrayList<>();
        for(Proposal p : proposals) {
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
            if(pd.isVisible()){
                flag = true;
                proposalsData.add(pd);
            }
        }
        List<Vote> principalVotes = voterService.getVoterByUsernameOrEmail(principal.getName()).getVotes();
        for(Vote v:principalVotes){
            String t = v.getTargetProposal().getBindingProcess().getTitle();
            if(Objects.equals(t, title)){
                hasVoted = true;
            }
        }
        if(hasVoted){
            model.addAttribute("proposalsList", Collections.emptyList());
        } else {
            model.addAttribute("proposalsList", proposalsData);
        }

        model.addAttribute("voterData", data);
        model.addAttribute("hasVoted", hasVoted);
        model.addAttribute("checkboxFlag", flag);
        model.addAttribute("currentElection", ed);

        return "auth-voter/user-ballot-box";
    }

    //------------------------------------------------------------------------------------------------------------------
    @PostMapping(value = "/user-ballot-box")
    public String voteEmission(@RequestParam(value = "selectedProposal", required = false) String proposal, Principal principal){
        voteService.addVote(proposal, principal);
        return "redirect:/auth-voter/user-elections-list?success";
    }

    //------------------------------------------------------------------------------------------------------------------
    @GetMapping(value = "/user-election-results")
    public String electionResultsView(@RequestParam(value = "uid", required = false) String title, final Model model, Principal principal) {
        VoterEntity ve = voterService.getVoterByUsernameOrEmail(principal.getName());
        VoterData data = new VoterData();
        data.setProfileImagePath(ve.getProfileImagePath());

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

        model.addAttribute("voterData", data);
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
                    prd.setPercent(prd.getVotes()*100/totalVoters); //limit to 2 the digits or cast to Integer
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
                tieProposalOne.setPercent(tieProposalOne.getVotes()*100/totalVoters);
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
                tieProposalTwo.setPercent(tieProposalOne.getVotes()*100/totalVoters);
                tieProposalTwo.setCreatedAt(p2.getTimestamp());

                totalVotes = totalVotes + tieProposalOne.getVotes() + tieProposalTwo.getVotes();
                participationPercent = totalVotes*100/totalVoters;

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

                return "auth-voter/user-election-results";
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
                    prd.setPercent(prd.getVotes()*100/totalVoters); //limit to 2 the digits or cast to Integer
                    prd.setCreatedAt(p.getTimestamp());
                    prd.setVisible(p.isVisible());
                    if(prd.isVisible()){
                        isVisible = true;
                        proposalsResultsList.add(prd); // order the adding in function of the number of votes or percent
                        totalVotes = totalVotes + prd.getVotes();
                    }
                }
                participationPercent = totalVotes*100/totalVoters; //limit to 2 the digits or cast to Integer

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

                return "auth-voter/user-election-results";
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
            prd.setPercent(prd.getVotes()*100/totalVoters);
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
        winner.setPercent(winner.getVotes()*100/totalVoters);
        winner.setCreatedAt(p.getTimestamp());

        totalVotes = totalVotes + winner.getVotes();
        participationPercent = totalVotes*100/totalVoters; //limit to 2 the digits or cast to Integer

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

        return "auth-voter/user-election-results";
    }
}
