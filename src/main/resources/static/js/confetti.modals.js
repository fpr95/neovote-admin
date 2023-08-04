        let winnerProposal = [[${winnerProposal}]];
        let tieProposalOne = [[${tieProposalOne}]];
        let tieProposalTwo = [[${tieProposalTwo}]];
        let conf = document.querySelector("#my-canvas");
        let confettiSettings = { target: 'my-canvas' };
        let confetti = new ConfettiGenerator(confettiSettings);
        confetti.render();

        $(document).ready(function(){
            if(winnerProposal != null){
                $("#winnerProposalModal").focus();
                $("#winnerProposalModal").modal('show');

                conf.classList.add('active');

                function onModalClosed() {
                    conf.classList.remove('active');
                }

                $("#winnerProposalModal").on("hidden.bs.modal", function() {
                    onModalClosed();
                });
            }
            if(tieProposalOne != null && tieProposalTwo != null){
                $("#tieModal").focus();
                $("#tieModal").modal('show');
            }
        });