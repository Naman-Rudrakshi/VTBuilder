//creates a column in which a square resides, in which an  image resides (redundant, can be reduced), with event listeners for when it is selected
//only called when bank HAS to be updated!
function createBankItem(url) {
  const column = document.createElement("div");
  column.classList.add("bank-column");
  column.id = url;

  const square = document.createElement("div");

  square.id = url;
  square.classList.add("image-square");

  const image = document.createElement("img");
  image.id = url; 
  image.src = url; 
  image.classList.add("bank-image");

  //adds event listener so if it is clicked, it is highlighted and selected is set to the right url
  //if selected and clicked it deselects, if something else is selected it replaces the url
  square.addEventListener("click", () => {
    var squareElements = document.getElementsByClassName("image-square");
    for (var i = 0; i < squareElements.length; i++) {
      squareElements[i].style.border = "3px solid transparent";
    }
    if (selected !== url) {
      selected = url;

      if (body.classList.contains('dark-mode')) {
    		square.style.border = "3px solid white";
	} else {
		square.style.border = "3px solid black";
	}
      
    } else {
      selected = null;
    }
  });

  square.appendChild(image); 
  column.appendChild(square);
  bank.appendChild(column);
}

//creates bank item for every item in banklist, clears all previous items
//ONLY CALLED WHEN BANK NEEDS AN UPDATE
function updateBank() {
  bank.innerHTML = ""; // Clear existing columns
  for (const link of bankList) {
      createBankItem(link);
  }
}

//called when clear grid button is clicked to reset player placements
function clearGrid() {
  for (let i = 0; i < server_occupied_list.length; i++) {
    const currentItem = server_occupied_list[i];
    if (currentItem === 1) {
      server_occupied_list[i] = 0;
      bankList.push(usedList[i]);
      usedList[i] = transp_link;
    }
  }
  updateBank();
  sendMySquares();
  updateGridImages();
}

