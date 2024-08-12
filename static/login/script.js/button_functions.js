//popup if the nickname or code arent filled out. Need to write logic later for if an invalid code is entered. (stage 192871092, unimportant rn)
const popup = document.getElementById("popup");
const okayButton = document.getElementById("okayButton");
const text = document.getElementById("popup-text");

//nickname and lobby input value variablbes
let nickname;
let lobby;

// Show the popup
function showPopup() {
  popup.style.display = "flex";
}
// Hide the popup
function hidePopup() {
  popup.style.display = "none";
}

//if the okay button is clicked in the popup, it waits a bit and then closes the popup
okayButton.addEventListener("click", () => {
    
    okayButton.classList.toggle("pressed");

    // Reset the class after a delay to simulate releasing the button
    setTimeout(() => {
        okayButton.classList.toggle("pressed");
    }, 300); // Adjust the delay (in milliseconds) as needed
    
    hidePopup();
});

//adds listeners for clicking all three buttons and performs the right functions. if a required field isnt filled, it does a popup.
document.addEventListener('DOMContentLoaded', () => {
    
  const joinLobbyButton = document.getElementById('joinLobbyButton');
  const createLobbyButton = document.getElementById('createLobbyButton');

  //join button
  joinLobbyButton.addEventListener('click', () => {
    joinLobbyButton.classList.toggle("pressed");

    // Reset the class after a delay to simulate releasing the button
    setTimeout(() => {
        joinLobbyButton.classList.toggle("pressed");
    }, 300); // Adjust the delay (in milliseconds) as needed

    //gets inputted values
    const lobbyCodeInput = document.getElementById('lobbyCode');
    lobby = lobbyCodeInput.value;
    const nicknameInput = document.getElementById('nickname');
    nickname = nicknameInput.value;

    //error handling if values are invalid/nonexistent
    if (nickname.length > 8) {
      text.textContent = "NICKNAME MUST BE LESS THAN 9 CHARACTERS";
      showPopup();
    }

    if (nickname && lobby) {
      if (nickname.length < 8) {
        const diff = 8-nickname.length;
        for (let i=0;i<diff;i++) {
          nickname += " ";
        }
      }
      joinLobby();

    } else if (lobby) {
      text.textContent = "ENTER NICKNAME (MAX 8 CHARACTERS)";
      showPopup();
    } else {
      text.textContent = "ENTER 4 DIGIT LOBBY CODE";
      showPopup();
    }

  });

  //create lobby
  createLobbyButton.addEventListener('click', () => {

    //gets value of name
    createLobbyButton.classList.toggle("pressed");
    const nicknameInput = document.getElementById('nickname');
    nickname = nicknameInput.value;

    // Reset the class after a delay to simulate releasing the button
    setTimeout(() => {
        createLobbyButton.classList.toggle("pressed");
    }, 300); // Adjust the delay (in milliseconds) as needed
    
    //error handling
    if (nickname) {
      if (nickname.length > 8) {
        text.textContent = "NICKNAME MUST BE LESS THAN 9 CHARACTERS";
        showPopup();
      }
      if (nickname.length < 8) {
        const diff = 8-nickname.length;
        for (let i=0;i<diff;i++) {
          nickname += " ";
        }
        createLobby();
      }
      
    } else {
      text.textContent = "ENTER NICKNAME (MAX 8 CHARACTERS)";
      showPopup();
    }
  });
});

