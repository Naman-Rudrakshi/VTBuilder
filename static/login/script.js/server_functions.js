//used to switch to a lobby page
function get_lobby(path, userid, lobby_code, method='get') {
	
    const form = document.createElement('form');
    form.method = method;
    form.action = path;

    const hiddenField3 = document.createElement('input');
    hiddenField3.type = 'hidden';
    hiddenField3.name = 'darkModeStatus'; 
    hiddenField3.value = darkModeStatus.toString();
    form.appendChild(hiddenField3);

    const hiddenField = document.createElement('input');
    hiddenField.type = 'hidden';
    hiddenField.name = 'userid'; 
    hiddenField.value = userid;
    form.appendChild(hiddenField);
    
    const hiddenField2 = document.createElement('input');
    hiddenField2.type = 'hidden';
    hiddenField2.name = 'lobby'; 
    hiddenField2.value = lobby_code;
    form.appendChild(hiddenField2);

   
    document.body.appendChild(form);
    form.submit();
    
}

//write another field into get to send the darkmode status

//sends nickname and code to server, recieves a path and user ID. user ID and path are then passed back to get an html page with the user ID (lobby)
async function joinLobby() {
    try {
        const response = await post("/send-join-lobby-data", {
            name: nickname,
            code: lobby
        }, 'application/json');
        console.log(response);
        get_lobby(response.url, response.code, response.lobby);
    } catch (error) {
        console.error(error);
        // You can re-throw the error if needed
        throw error;
    }    
}

//same as above, but without a code, as the server creates and assigns the code
async function createLobby() {
    try {
        const response = await post("/create-lobby", {
            name: nickname,
        }, 'application/json');
        get_lobby(response.url, response.code, response.lobby);
    } catch (error) {
        console.error(error);
        // You can re-throw the error if needed
        throw error;
    }    
}