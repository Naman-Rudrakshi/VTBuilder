//gets unique user id from the thingy in the search bar or whatever
const userID = window.location.search.substring(25,27);

//player list that will be iterated through to display the players. first item is just a title for the player list
let player_list = ["Players"];

// used to ask the server for something and recieve something back, or to just send it something
function post(path, requestData, content_type) {
	return new Promise((resolve, reject) => {
		fetch(path, {
		method: 'POST',
		headers: {
			'Content-Type': content_type
		},
		body: JSON.stringify(requestData)
		}).then(response => response.json())
		.then(data => {
			resolve(data);
		})
		.catch(error => {
			reject(error);
		});
	});
}

// used specifically to ask the server to switch pages
function get(path, code, method='get') {
	
    const form = document.createElement('form');
    form.method = method;
    form.action = path;

    const hiddenField2 = document.createElement('input');
    hiddenField2.type = 'hidden';
    hiddenField2.name = 'darkModeStatus'; 
    hiddenField2.value = darkModeStatus;
    form.appendChild(hiddenField2);

    const hiddenField = document.createElement('input');
    hiddenField.type = 'hidden';
    hiddenField.name = 'userid'; 
    hiddenField.value = code;
    form.appendChild(hiddenField);
    
    document.body.appendChild(form);
    form.submit(); 

}

//function specifically to swicth the login page
function get_login(path, method='get') {
	
  console.log("FIRSTTT");
	
  const form = document.createElement('form');
  form.method = method;
  form.action = path;
  

  const hiddenField = document.createElement('input');
  hiddenField.type = 'hidden';
  hiddenField.name = 'darkModeStatus'; 
  hiddenField.value = darkModeStatus;
  form.appendChild(hiddenField);

	
  document.body.appendChild(form);
  form.submit();
  
}

//done
//function specifically for when the leave game button is clicked. returns you to login
async function leaveGame() {
	
	try {
        post("/leave-game", {
          userid: userID
        }, 'application/json');
        
        get_login("/login");
        
    } catch (error) {
        console.error(error);
    }  
    
}

//updates player_list if the inputted list of names is different
function playerListUpdater(names) {
  //temporary variable for incoming list
  let playerList = ["Players"];
	
  //listify's the string of names. the character limit for each name is 8 characters
	for (let i=0;i<(names.length);i+=8) {
		const sub = names.substring(i,i+8);
		playerList.push(sub);
	}
	
  //updates player_list only if new list is different. then updates the font size as it may have reset.
  if (JSON.stringify(player_list) !== JSON.stringify(playerList)) {
      player_list = playerList;
      updatePlayers();
      updateFontSize();   
  }
}

//updates list of players on the screen. Called in the game status function, which is called every second
function updatePlayers() {
  //list of players container
  const listBox = document.getElementById("player-list");
  listBox.innerHTML = ""; // Clear previous content

  //adds all the player names in player_list to the display box
  player_list.forEach((player, index) => {
    const playerElement = document.createElement("div");
    const playerHolder = document.createElement("div");
    playerHolder.classList.add("playerRow")
    playerHolder.classList.add("row-container")
    
    playerElement.classList.add("input");
    playerElement.classList.add("playerText");
    playerElement.id = "player";
    playerElement.textContent = player;

    //makes the first name be the player listbox title (different css)
    //the second name is the first player (the current user), which has different css
    if (index === 0) {
      playerHolder.id = "player-title";
    }
    if (index === 1) {
      playerHolder.id = "first-player";
    }

    //adds everything to the listbox 
    playerHolder.appendChild(playerElement)
    listBox.appendChild(playerHolder);
  });
}

function stringify(listItem) {
  if (listItem > 9) {
    return "" + listItem;
  } else {
    return "0" + listItem;
  }
}

function listify(stringItem, interval) {
  let temporary = [];
  for (let i = 0; i < stringItem.length; i+=interval) {
    temporary.push(parseInt(stringItem.substring(i,i+interval)));
  }
  return temporary
}


