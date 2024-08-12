/* populates assembledList with links to the smaller images */
function populateAssembledList() {

  /* creates a canvas object but doesn't place it anywhere*/
  const canvas = document.createElement("canvas");
  const ctx = canvas.getContext("2d");

  // Set canvas size to the same as the original image
  canvas.width = refImage.width;
  canvas.height = refImage.height;
 
  // Draw the linked image on the canvas
  ctx.drawImage(refImage, 0, 0); 
	console.log(assembledList);
  // Loop through the coordinates in the final assembled list to create the corresponding images
  for (number of assembledList) {

    if (number !== "xx") {
      /*width and height are always gonna be 1280, but just in case :) */
      const squareWidth = canvas.width / rows;
      const squareHeight = canvas.height / rows;

      /* row number and column number (coordinate) of an image square based on original number*/
      const row = Math.floor(number / rows);
      const col = number % rows;

      // Create a new canvas for each square
      const squareCanvas = document.createElement("canvas");
      squareCanvas.width = squareWidth;
      squareCanvas.height = squareHeight;
      const squareCtx = squareCanvas.getContext("2d");

      // Extract the square from the original image and draw it on the new canvas
      squareCtx.drawImage(
        canvas,
        col * squareWidth, row * squareHeight,
        squareWidth, squareHeight,
        0, 0,
        squareWidth, squareHeight
      );

      // Generate a new image URL for the square and add it to the assembled url list
      const newImageUrl = squareCanvas.toDataURL("image/jpeg"); // Or "image/png"
      console.log(newImageUrl);
      assembledURLs.push(newImageUrl);
    } else {
      assembledURLs.push(transp_link);
    }
    
  }
}

// updates all the images in the assembled grid. 
// obj is the square grid object, im is the image object on top, number is the single coordinate number of the image/grid object
function updateImages(obj, im, number) {
  obj.style.backgroundColor = "transparent";
  im.src = assembledURLs[number];
}

//updates assmbled grid when resized. just like game grid in main game
function updateGridDimensions() {

  //find width of the current game grid and calculate each square size
  const assembledWidth = assembledSquare.offsetWidth;
  const squareSize = (assembledWidth/rows) - gapSize; // Adjust the size of each square as needed

  // add style for number of rows, columns (figure out what exactly is going on lol)
  assembledSquare.style.gridTemplateColumns = `repeat(${rows}, ${squareSize}px)`;
  assembledSquare.style.gridTemplateRows = `repeat(${rows}, ${squareSize}px)`;

  // Remove existing grid items
  while (assembledSquare.firstChild) {
      assembledSquare.removeChild(assembledSquare.firstChild);
  }

  // Create new grid squares
  //gridItem2 is a terrible class name, but its to differentiate between the grid items from the background
  for (let i = 0; i < total_squares; i++) {
    const gridItem2 = document.createElement("div");
    gridItem2.classList.add("grid-item2");
    const img = document.createElement("img");
    img.classList.add("image");
    img.src = assembledURLs[i]; // Set an initial empty source link
    gridItem2.appendChild(img); 
    //add  the event listeners
    updateImages(gridItem2, img, i);
    assembledSquare.appendChild(gridItem2);
  }
}

