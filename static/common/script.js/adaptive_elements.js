//background circles container
const gridContainer = document.querySelector('.background-grid');

//list of all elements with text in them (except player names). This is primarily for buttons and labels
let elements = document.querySelectorAll('.text');

//resizes all the circles in the grid by deleting and creating new grid items
function updateGrid() {
  const squareSize = 50; // Adjust the size of each square as needed
  const gapSize = 0; // Adjust the gap between squares as needed

  const screenWidth = window.innerWidth;
  const screenHeight = window.innerHeight;

  const numColumns = Math.ceil(screenWidth / (squareSize + gapSize));
  const numRows = Math.ceil(screenHeight / (squareSize + gapSize));
  const totalSquares = numColumns * numRows;

  gridContainer.style.gridTemplateColumns = `repeat(${numColumns}, ${squareSize}px)`;
  gridContainer.style.gridTemplateRows = `repeat(${numRows}, ${squareSize}px)`;

  // Remove existing grid items
  while (gridContainer.firstChild) {
    gridContainer.removeChild(gridContainer.firstChild);
  }

  // Create new grid items
  for (let i = 0; i < numRows; i++) {
    for (let k = 0; k <numColumns;k++) {

      //circles increase in size going right and going down (just like a periodic trend 8D)
      const circle = document.createElement('div');
      const radius = (i*k/totalSquares * 100 + 10 ); //arbitrarily chosen radius (im pretty sure its non linear idk)
      circle.style.width = `${radius}%`; 
      circle.style.height = `${radius}%`;
      circle.setAttribute("data-type", "mode");
      
      //adds class and appends to grid
      circle.classList.add("background-circle");
      gridContainer.appendChild(circle);
    }
    
  }
}
//resizes almost all text elements (except the things at the top, which are not dynamically changing)
function updateFontSize() {
  //determines font size based on the second text element's container (should always be a button), might need to adjust this later
  let font;
  if (elements.length > 1) {
    font = elements[1].offsetHeight * 0.20;
  }
  
  if (elements.length==1) {
    font = elements[0].offsetHeight;
  }

  //changes all font sizes for .text elements
  elements.forEach(element => {
    element.style.fontSize = font + 'px';
  });
  
  //specifically for player names, same functionality, but seperated since player names are a different size
  if (document.querySelector('.playerText')) {
    let playerelements = document.querySelectorAll('.playerText');
    let playerfont = playerelements[0].offsetHeight*0.9;
    playerelements.forEach(element => {
      element.style.fontSize = playerfont + 'px';
    });
  }

  if (document.querySelector('.slider-text')) {
    let playerelements = document.querySelectorAll('.slider-text');
    let playerfont = playerelements[0].offsetHeight*0.35;
    
    playerelements.forEach(element => {
      element.style.fontSize = playerfont + 'px';
    });
  }

  if (document.querySelector('.reflection-text')) {
    let playerelements = document.querySelectorAll('.reflection-text');
    let playerfont = playerelements[0].offsetHeight*0.4;
    
    playerelements.forEach(element => {
      element.style.fontSize = playerfont + 'px';
    });
  }

  if (document.querySelector('.entry-text')) {
    let playerelements = document.querySelectorAll('.entry-text');
    let playerfont = playerelements[0].offsetHeight*0.05;
    
    playerelements.forEach(element => {
      element.style.fontSize = playerfont + 'px';
    });
  }
  /*
  if (document.querySelector('.popup-text')) {
    let playerelements = document.querySelectorAll('.popup-text');
    let playerfont = (playerelements[0].offsetHeight)*(playerelements[0].offsetWidth)*0.001;
    console.log(playerelements[0].offsetHeight);
    console.log(playerfont);
    playerelements.forEach(element => {
      element.style.fontSize = playerfont + 'px';
    });
  }
  */
}
//function to make the layout two columns or two rows based on the window dimensions
function updateLayout() {
  
  //thing that holds both columns/rows
  let container;
  if (document.getElementById('largeColumnContainer')) {
    container = document.getElementById('largeColumnContainer');

  } else {
    container = document.getElementById('page-3');
    
  }
   

  const windowWidth = window.innerWidth;
  const windowHeight = window.innerHeight;

  const columnOne = document.getElementById('column-one');
  const columnTwo = document.getElementById('column-two');

  //mathematically calculated i swear, its not guess and check numbers. This just stacks the two boxes as rows if the window as vertical
  //and as columns if it is horizontal
  if (windowWidth < windowHeight) {
    container.style.flexDirection = 'column';
    
      columnOne.style.width = '100%';
      columnOne.style.height = '48%';
      columnOne.style.justifyContent = 'center';

      columnTwo.style.width = '100%';
      columnTwo.style.height = '48%';
      columnTwo.style.justifyContent = 'center';
    
 
  } else {
    container.style.flexDirection = 'row';

    columnOne.style.width = '48%';
    columnOne.style.height = '100%';

    columnTwo.style.width = '48%';
    columnTwo.style.height = '100%';
    
    columnOne.style.justifyContent = 'right';
    columnTwo.style.justifyContent = 'left';
    
  }

  //this whole section determines whether the squares in each column are restricted by the window width or height
  //it sets it to 80% of the width or height, whichever is less
  const columnWidth = columnOne.offsetWidth;
  const columnHeight = columnOne.offsetHeight;

  if ((columnWidth) < columnHeight) {
    
    const squareContainer = document.getElementById('gameSquare');
    squareContainer.style.width = '80%';
    squareContainer.style.height = squareContainer.offsetWidth + 'px';

    const left = document.getElementById("left-square");
    left.style.width = "80%";
    left.style.height = left.offsetWidth + "px";

  } else {
   
    const squareContainer = document.getElementById('gameSquare');
    squareContainer.style.height = '80%';
    squareContainer.style.width = squareContainer.offsetHeight + 'px';

    const left = document.getElementById("left-square");
    left.style.height = "80%";
    left.style.width = left.offsetHeight + "px";

  };

}
//resizes everything that needs to be resized (except the game grid, which is not a common function)
//login doesnt have the large container, so there is an if statement
function resizeElements() {
  updateGrid();
  if (document.getElementById('largeColumnContainer') || document.getElementById('page-3')) {
	  updateLayout();
  }
  updateFontSize();
}

//creates an event listener for the dark mode toggle button
//when toggled, it changes the src for the logo on top, and changes all elements to habe the dark mode class
function darkMode() {
  darkModeToggle = document.getElementById("darkModeToggle");    
  body = document.body;
	
	console.log(parseInt(window.location.search.substring(16,17)));
  if (parseInt(window.location.search.substring(16,17)) === 1) {
	  console.log("true");
    darkModeStatus = 1;
    body.classList.add("dark-mode");
  } else {
    darkModeStatus = 0;
  }
  
  if (darkModeStatus === 1) {
        logo.src = "images/gameLogo.png";
      } else if (darkModeStatus === 0) {
        logo.src = "images/darkgameLogo.png";
      }

  darkModeToggle.addEventListener("click", function () {
      darkModeStatus = 1 - darkModeStatus;

      const logo = document.getElementById("logo");
      
      if (darkModeStatus === 1) {
        logo.src = "images/gameLogo.png";
      } else if (darkModeStatus === 0) {
        logo.src = "images/darkgameLogo.png";
      }
      
      body.classList.toggle("dark-mode");
      
  });
}

//calls dark mode, initial element sizing, and an event listener for a screen resize
darkMode();
resizeElements();
window.addEventListener('resize',resizeElements);
