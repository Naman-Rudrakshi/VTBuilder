/* internet link for a transparent overlay and a downloaded link for a striped overlay, can be downloaded file idk */
const transp_link = "https://upload.wikimedia.org/wikipedia/commons/thumb/8/89/HD_transparent_picture.png/1200px-HD_transparent_picture.png";
const stripe_link = 'images/stripeOverlay.png';

/* tempBank: list for number coordinates of the images assigned to this user. The original image is split into a grid of squares.
each side length is equal to the sqrt of the total number of squares. the squares are designated one number, 0 through total_squares-1, 
left to right top to bottom*/
let tempBank = [];

//tempDict holds links as keys and their corresponding original grid placement as values. This is used when sendinf final grid placement data to server at the end of teh game
let tempDict = {};

/* list of all the links of the images assigned to this user. used to create the image selection options on the bottom left corner.*/
let bankList = [];

/* list of the image url being used at every square on the grid. empty squares, including ones occupied by other players, have a
transparent image. initialized with all transparent images*/
let usedList=  [];

/*0 = unoccupied, 1 =occupied by user, 2 = occupied by someone else*/
//player's personal list. this gets updated by the server if there is a change. need to add functionality if the server
//is inconsistent with what the user has occupied
let server_occupied_list = [];

//live occupied list (this is what comes from the server)
let occupied_id_list = "";

/* bank refers to image bank, like a wordbank on a worksheet but for images lol */
const bank = document.getElementById("bank");

const gapSize = 1; // Adjust the gap between squares as needed (for gameplay grid)

// link to the currently selected image
let selected = null;

//number of times a player places/removes something (alters the board)
let num_changes = 0;
let score;
let max_distance;

getStarterData();

//done
//function specifically for the original data. also runs the rest of the program dependent on this data
async function getStarterData() {
  try {
      const response = await post("/get-game-initial-data", {
        userid: userID
      }, 'application/json');
      
      //number of squares on the grid (needed to get pieces of the image and make grid)
      total_squares = parseInt(response.numSquares); 
      for (let i=0; i<total_squares;i++) {
        usedList.push(transp_link);
        server_occupied_list.push(0);
      }

      //the greatest possible distance between two squares.
      //sqrt of total number of squares is the length of each side. subtract one for the distance from square zero to the end square.
      //raise this to second power
      //multiple it by 2 because we are finding the hypotenuse of a isoscleles right triangle
      //sqrt it by pythagorean theorem
      if (total_squares > 1) {
		  max_distance = Math.sqrt(Math.pow((Math.floor(Math.sqrt(total_squares)) - 1),2) * 2);
		} else {
			max_distance = 1;
		}
      //which pieces are assigned to this user specifically
      numbersString = response.assignedSquares;
      for (let i = 0; i < numbersString.length; i += 2) {
        const substring = numbersString.substring(i, i + 2);
        const intValue = parseInt(substring);
        tempBank.push(intValue);
      }

      /* creates HTML image element to be used later in order to create smaller images*/
      //might need to make img url constant later
      img_url = response.imageUrl+"?userid=" + userID;
      refImage = document.createElement("img");
      refImage.src = img_url;

      //once images has loaded, the rest of the program is run
      refImage.addEventListener("load", callFunctions);
                
  } catch (error) {
      console.error(error);
      // You can re-throw the error if needed
      throw error;
  }
}

function accuracyScore(d) {
  return d/max_distance;
  //make it a curve or something idk
}

//done
//fucntion that gets final square placement and asks for post game path, redirects
async function sendFinalData() {

  //list for final squares (by coordinate number 0 (numsquares-1)) occupied by this user
  let occupied = [];
	
  for (i=0;i<usedList.length;i++) {
    occupied.push("xx");
    if (usedList[i] !== transp_link && usedList[i] !== stripe_link) {
      occupied[i] = tempDict[usedList[i]];
    }
  }

  //converts to string
  let occupiedStr = "";
  for (i=0; i<occupied.length;i++) {
    if (occupied[i] === "xx") {
      occupiedStr += "xx"
    } else {
      //stringify
      if (occupied[i] < 10) {
        occupiedStr += ("0" + occupied[i]);
      } else {
        occupiedStr += occupied[i];
      }
    }
    
  }

  let tempIndex = 0;
  let score = 0;

  //calculates the average accuracy of the placements of this player by distance
  for (item of occupied) {
    if (item !== "xx") {

	width = Math.sqrt(total_squares);
      let player_x = tempIndex%width;
      let player_y = Math.floor(tempIndex/width);

      let original_x = item%width;
      let original_y = Math.floor(item/width);
	
	console.log("player x ", player_x);
	console.log("player y ", player_y);
	console.log("original x ", original_x);
	console.log("original y ", original_y);
      //pythagorean theorem
      //first take the difference between x's and square, then do the same for the y's and sum them. take the sqrt of all this.
      //this is the distance between the placed square and where its supposed to go.
      let distance = Math.sqrt((Math.pow((player_x-original_x),2)+Math.pow((player_y-original_y),2))) 
      
      //this adds each tile's accuracy to score (where 1 is least accurate and 0 us most)
      //instead of dividing score by the number of data points at the end, it does it while adding data
      console.log("sq ", item);
      console.log("distance ", distance);
      console.log("squareacc ", accuracyScore(distance)/tempBank.length);
     
      score += accuracyScore(distance)/tempBank.length;
      
    }
    tempIndex++;
  }

	console.log("max ", max_distance);
  //score is noe from 0 to 100 with two decimals, where 0 is on the opposite side of the board and 100 is dead on
  score = ((1-score)*100).toFixed(2);
	console.log("scire ", score);
	
	let keys = Object.keys(wordCountDict);
	let values = Object.values(wordCountDict);
	
	
  try {
	  
      const response = await post("/send-post-game-transfer-data", {
          userid: userID,
          occupiedSquares: occupiedStr,
          playerAccuracy: score,
          numPlacements: num_changes,
          keyList:keys,
          valueList:values
      }, 'application/json');
      get(response.postGamePath, userID);
  } catch (error) {
      console.error(error);
      // You can re-throw the error if needed
      throw error;
  }
}

//done
//function specifically for the grid status data
//add a function that says game over and waits a few seconds before actually switching screens when the game is over(stage 2, dw rn)
async function getGameState() {
  try {
      const response = await post("/get-square-locations-data", {
        userid: userID
      }, 'application/json');

      //if game is over, send final data and switch to next page
      if (response.gameStatus === "2") {
		  clearInterval(stateInterval);
        await sendFinalData();
      } 

      //temp list for data recieved from the server
      let new_server_occupied_list = [];

      //string of recieved data for later comparison
      occupied_id_list = response.occupiedList;
      
      //creates new server occupied list
      for (let i=0; i<occupied_id_list.length;i+=2) {
        if (occupied_id_list.substring(i,i+2) === userID) {
          new_server_occupied_list.push(1);
        } else if (occupied_id_list.substring(i,i+2) === "00") {
          new_server_occupied_list.push(0);
        } else {
          new_server_occupied_list.push(2);
        }
      }

      //update checking protocol later
      //checks and updates personal list only if new information is different. add logic for when server info conflicts with what user has occupied
      if (JSON.stringify(new_server_occupied_list) !== JSON.stringify(server_occupied_list)) {
        for (let i=0;i<new_server_occupied_list.length;i++) {
          if (server_occupied_list[i] !== 1) {
            if (new_server_occupied_list[i] === 0) {
			  server_occupied_list[i] = new_server_occupied_list[i];
              usedList[i] = transp_link;
            } else if (new_server_occupied_list[i] === 2) {
              usedList[i] = stripe_link;
              server_occupied_list[i] = new_server_occupied_list[i];
            }
          }
        }
        updateGridImages();
      }
      //updates list of players
      playerListUpdater(response.userList);

  } catch (error) {
      console.error(error);
      // You can re-throw the error if needed
      throw error;
  }
}

//done
//function specifically for sending my squares to the server when i make a placement
async function sendMySquares() {

  //list of personally occupied squares
  let occupiedString = "";

  //stringifies server occupied list and adds other player IDs too
  for (let i=0;i<server_occupied_list.length;i++) {
     if (server_occupied_list[i] === 1) {
	    occupiedString+=userID;
    } else if (server_occupied_list[i] === 2) {
	    occupiedString+= occupied_id_list.substring(2*i,2*i+2);
    } else {
	    occupiedString+= "00";
    }
  }

  try {
    await post("/send-square-locations-data", {
      userid: userID,
      occupiedSquares : occupiedString
    }, 'application/json');
      
  } catch (error) {
      console.error(error);
  }  
}

//done
//sends a post to change game status to 2
async function endGame() {
  let time = (stopTimer()).toString();
  console.log(time);
  try {
    const response = await post("/end-game", {
      userid: userID,
      timeElapsed: time
    }, 'application/json');
    console.log(response.userid);
      
  } catch (error) {
      console.error(error);
  }  
}

//rest of the program (dependent on started dtata)
function callFunctions() {

  /* the container in which the gameplay grid resides*/
  gameSquare = document.getElementById("gameSquare");

  /* constants for number of rows/columns*/
  rows = Math.sqrt(total_squares);

  //populates bankList only after the image has loaded, or else the image will just be black (this function is called after load*/
  populateBankList();
  updateBank();
  updateGridSize();
  
  //event listener for resizing the game grid
  window.addEventListener('resize', updateGridSize);

  

  const interval = 1000; // 1000 milliseconds = 1 second
  stateInterval = setInterval(getGameState, interval); // every second, calls getGameState, which assigns an updated value to server occupied list and checks if the game is over 
  
  //timer widget in the corner. should be more or less the same for everyone
  startTimer();
}
