
    let voteButtons = document.getElementsByClassName("btn-vote");
    let confirmButton = document.getElementById("confirmation-btn");
    let isVoteSelected = false;
    for (var i = 0; i < voteButtons.length; i++) {
        voteButtons[i].addEventListener("click", function(event) {
            // Disable all the other vote buttons
            for (var j = 0; j < voteButtons.length; j++) {
                if (voteButtons[j] !== event.target) {
                    voteButtons[j].disabled = !voteButtons[j].disabled;
                    if (voteButtons[j].classList.contains("btn-success")) {
                        voteButtons[j].classList.remove("btn-success");
                        voteButtons[j].classList.add("btn-primary");
                    }
                    if (voteButtons[j].innerText = "Votado") {
                        voteButtons[j].innerText = "Votar";
                    }
                    if (voteButtons[j].disabled >= 1) {
                        confirmButton.disabled = false;
                    }
                    if (voteButtons[j].disabled == false) {
                        confirmButton.setAttribute('data-mdb-target', '#');
                    } else {
                        confirmButton.setAttribute('data-mdb-target', '#voteConfirmationModal');
                    }
                }
            }
            let btn = event.target;
            let isVoted = btn.innerText == "Votar";
            if (!isVoted) {
                btn.innerText = "Votado";
                btn.classList.remove("btn-primary");
                btn.classList.add("btn-success");
            }
            document.getElementById("selectedProposal").value = btn.getAttribute("data-proposal-name");
            document.getElementById("selectedProposal").name = "selectedProposal";
        });
    }
