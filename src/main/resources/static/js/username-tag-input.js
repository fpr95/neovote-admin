        const ul = document.querySelector(".ul-test");
        const input = document.querySelector(".input-test");
        maxNumb = document.querySelector(".details-box-1 span");

        let maxTags = 100,
        tags = [];

        countTag();

        function countTag(){
            input.focus();
            maxNumb.innerText = maxTags - tags.length - currentRolSize;
        }

        function createTag(){
        //this should avoid duplicates removing all <li>s before adding
            ul.querySelectorAll("li").forEach(li => li.remove());
            tags.slice().reverse().forEach(tag =>{
                let liTag = `<li class="li-test">${tag} <i class="fa fa-circle-xmark ms-1" onclick="remove(this, '${tag}')"></i> </li>`;
                ul.insertAdjacentHTML("afterbegin", liTag)
            });
            countTag();
        }

        function remove(element, tag){
            //get tag index
            let index = tags.indexOf(tag);
            //removing or excluding the selected tag from the array
            tags = [...tags.slice(0, index), ...tags.slice(index + 1)];
            element.parentElement.remove();
            countTag();
        }

        function addTag(e){
            if(e.key == "Enter"){
                let tag = e.target.value.replace(/\s+/g, ' ');
                if(tag.length > 2 && !tags.includes(tag)){
                    if(tags.length < maxTags){
                        tag.split(',').forEach(tag =>{
                            tags.push(tag);
                            createTag();
                        });
                    }
                }
                e.target.value = "";
            }
        }

        input.addEventListener("keyup", addTag);

        const removeBtn = document.querySelector(".remove-all-btn");
        removeBtn.addEventListener("click", () =>{
            tags.length = 0;
            ul.querySelectorAll("li").forEach(li => li.remove());
            countTag();
        })

        function addTagsToInput(){
            input.value += tags;
        }