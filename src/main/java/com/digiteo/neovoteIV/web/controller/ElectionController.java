package com.digiteo.neovoteIV.web.controller;

import com.digiteo.neovoteIV.model.service.ElectionService;
import com.digiteo.neovoteIV.system.exception.ElectionAlreadyExistException;
import com.digiteo.neovoteIV.web.data.model.ElectionData;
import com.digiteo.neovoteIV.web.data.model.ElectionListData;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@RequestMapping("/auth")
@Controller
@AllArgsConstructor
public class ElectionController {

    private ElectionService electionService;

    @GetMapping("/election-new")
    public String newElectionView(final Model model){
        model.addAttribute("electionData", new ElectionData());
        return "auth/election-new";
    }

    @PostMapping("/election-new")
    public String createNewElection(final @Valid ElectionData electionData, final BindingResult bindingResult, final Model model, Principal principal){
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
}
