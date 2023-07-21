package com.digiteo.neovoteIV.web.controller;

import com.digiteo.neovoteIV.model.jpa.data.Election;
import com.digiteo.neovoteIV.model.jpa.data.Proposal;
import com.digiteo.neovoteIV.model.mapper.ElectionMapper;
import com.digiteo.neovoteIV.model.service.ElectionService;
import com.digiteo.neovoteIV.model.service.VoteService;
import com.digiteo.neovoteIV.web.data.model.ElectionData;
import com.digiteo.neovoteIV.web.data.model.ElectionListData;
import com.digiteo.neovoteIV.web.data.model.ProposalData;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RequestMapping("/auth-voter")
@Controller
@AllArgsConstructor
public class UserElectionController {

    private ElectionService electionService;
    private ElectionMapper electionMapper;
    private VoteService voteService;

    @GetMapping("/user-elections-list")
    public String electionsListView(final Model model){
        List<ElectionListData> eld = electionService.getVotersElectionsList();
        model.addAttribute("voterElectionsList", eld);
        return "auth-voter/user-elections-list";
    }

    @GetMapping("/user-election-details")
    public String electionDetailsView(@RequestParam(value = "uid", required = false) String title, final Model model) {
        Election e = electionService.getElectionByTitle(title);
        ElectionData ed = electionMapper.toDto(e);
        boolean flag = false; // check if this flag is necessary, if not, take it out
        List<Proposal> proposals = e.getProposals(); // the fetch type for the proposals is LAZY
        List<ProposalData> proposalsData = new ArrayList<>();
        for(Proposal p : proposals) {
            ProposalData pd = new ProposalData();
            pd.setName(p.getName());
            pd.setContactEmail(p.getContactEmail());
            pd.setDetails(p.getDetails());
            pd.setVisible(p.isVisible());
            pd.setCreatedAt(p.getTimestamp());
            if(pd.isVisible()){
                flag = true;
                proposalsData.add(pd);
            }
        }
        model.addAttribute("checkboxFlag", flag); // check if this flag is necessary, if not, take it out
        model.addAttribute("currentElection", ed);
        model.addAttribute("proposalsList", proposalsData);

        return "auth-voter/user-election-details";
    }

    @GetMapping("/user-ballot-box")
    public String ballotBoxView(@RequestParam(value = "uid", required = false) String title, final Model model){
        Election e = electionService.getElectionByTitle(title);
        ElectionData ed = electionMapper.toDto(e);
        boolean flag = false;
        List<Proposal> proposals = e.getProposals();
        List<ProposalData> proposalsData = new ArrayList<>();
        for(Proposal p : proposals) {
            ProposalData pd = new ProposalData();
            pd.setName(p.getName());
            pd.setContactEmail(p.getContactEmail());
            pd.setDetails(p.getDetails());
            pd.setVisible(p.isVisible());
            pd.setCreatedAt(p.getTimestamp());
            if(pd.isVisible()){
                flag = true;
                proposalsData.add(pd);
            }
        }
        model.addAttribute("checkboxFlag", flag);
        model.addAttribute("currentElection", ed);
        model.addAttribute("proposalsList", proposalsData);

        return "auth-voter/user-ballot-box";
    }

    @PostMapping(value = "/user-ballot-box")
    public String voteEmission(@RequestParam(value = "selectedProposal", required = false) String proposal, Principal principal){
        voteService.addVote(proposal, principal);
        return "redirect:/auth-voter/user-elections-list?success";
    }
}
