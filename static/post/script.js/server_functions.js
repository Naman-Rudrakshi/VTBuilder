

//done
//sends the two slider data points to the server. in another function, it switches to the loading page
async function sendSurveyData() {
    try {
		const val1 = slider1Value.toString();
		const val2 = slider2Value.toString();
        const response = await post("/send-survey-data", {
          userid: userID,
          selfRating: val1,
          teamRating: val2
        }, 'application/json');
		
		console.log(response.userid);
		
		//timer
        analyticTimer = setInterval(getAnalytics, 3000);
          
    } catch (error) {
        console.error(error);
        // You can re-throw the error if needed
        throw error;
    }
}

async function getAnalytics() {
	try {
        const response = await post("/recieve-analytics", {
          userid: userID,
        }, 'application/json');
		
		if (response.status === "1") {
			clearInterval(analyticTimer);
			categorizedWeightsKeys = response.key1.slice(1);
		    categorizedWeightsValues = response.val1.slice(1);
		    
		    impactDictKeys = response.key2.slice(1);
		    impactDictValues = response.val2.slice(1);
		    
		    categoryCountKeys = response.key3.slice(1);
		    categoryCountValues = response.val3.slice(1);
		    
		    wordCountKeys = response.key4.slice(1);
		    wordCountValues = response.val4.slice(1);

            catWeightDict = combineListsToDictionary(categorizedWeightsKeys, categorizedWeightsValues);
            impactDict = combineListsToDictionary(impactDictKeys,impactDictValues);
            catCountDict = combineListsToDictionary(categoryCountKeys,categoryCountValues);
            wordCountDict = combineListsToDictionary(wordCountKeys,wordCountValues);
``			
			for (const key in impactDict) {
			  if (impactDict[key] === 0) {
			    delete impactDict[key];
			  }
			}
			
			for (const key in wordCountDict) {
			  if (wordCountDict[key] === 0) {
			    delete wordCountDict[key];
			  }
			}
			
			
			console.log(catWeightDict);
			console.log(impactDict);
			console.log(catCountDict);
			console.log(wordCountDict);
            createBarGraph("chart-container1", catWeightDict, "Graph 1"); // Pass the graph title
            createBarGraph("chart-container2", catCountDict, "Graph 2"); // Pass the graph title

            wordCloudData = impactDict;
            const fontSize = d3.scaleLinear()
    .domain([0, d3.max(Object.values(wordCloudData))])
    .range([10, 30]);

const layout = d3.layout.cloud()
    .size([containerWidth, containerHeight])
    .words(Object.keys(wordCloudData).map(word => {
        return {
            text: word,
            size: fontSize(Math.abs(wordCloudData[word])),
            sign: Math.sign(wordCloudData[word]),
        };
    }))
    .padding(5)
    .rotate(() => 0)
    .font('Impact')
    .fontSize(d => d.size)
    .on('end', drawWordCloud);
            layout.start();
			getStarterData();
		}
		
   		
          
    } catch (error) {
        console.error(error);
        // You can re-throw the error if needed
        throw error;
    }
}
//function specifically for the original data. calls rest of program (dependent on original data) after
async function getStarterData() {
    try {
        const response = await post("/send-post-game-initial-data", {
          userid: userID
        }, 'application/json');
        

        //change in MAIN GAME as well. link to original image
        img_url = "/get-image-from-server48281" + "?userid=" + userID;

        //list of all the grid placements of the team assembled image
        assembledList = [];
        //adds all coordinates to the list
        numbersString = response.finalOccupiedList;
        console.log("string " + numbersString);
        for (let i = 0; i < numbersString.length; i += 2) {
            const substring = numbersString.substring(i, i + 2);
            let intValue;
            if (substring != "xx") {
                intValue = parseInt(substring);
            } else {
                intValue = "xx"
            }
            assembledList.push(intValue);
        }
        
        teamScore = response.teamAccuracy;
        

        //number of squares on the grid (needed to get pieces of the image and make grid), and num of rows
        total_squares = (assembledList).length;

        //CREATE AND IMPLEMENT A STRINGIFY/LISTIFY FUNCTION

        //list for the corresponding urls. similar to tempDict, populated in final_grid
        assembledURLs = [];

        //calls program after image has loaded
        
        
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

//function for switching to the lobby screen 
async function returnToLobby() {
	leaveGame();
	/*
    try {
		
        const response = await post("/return-to-lobby", {
          userid: userID
        }, 'application/json');
        let nickname = response.name;
        
        if (response.lobbyCode === "-1") {
			try {
		        const response = await post("/create-lobby", {
		            name: nickname,
		        }, 'application/json');
		        
		        sendNewCode(response.lobby);
		        get_lobby(response.url, response.code, response.lobby);
		    } catch (error) {
		        console.error(error);
		        // You can re-throw the error if needed
		        throw error;
		    }    
			
		} else {
			joinLobby(nickname,response.lobbyCode);
		}
        
    } catch (error) {
        console.error(error);
        // You can re-throw the error if needed
        throw error;
    }
    */
}

//rest of program
//add button functions
function callFunctions() {
    endAnimation();
    showPage(3);

    /* the container in which the assembled grid resides*/
    assembledSquare = document.getElementById("gameSquare");
    gapSize = 0;

    /* constants for number of rows/columns*/
    rows = Math.sqrt(total_squares);

    //populates list of urls
    populateAssembledList();

    //resizing grid
    window.addEventListener('resize', updateGridDimensions);
    updateGridDimensions();

    //puts original image onto the screen
    finalImage = document.querySelector('.finalImage');
    finalImage.src = img_url;

    
}


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
/*
//sends nickname and code to server, recieves a path and user ID. user ID and path are then passed back to get an html page with the user ID (lobby)
async function joinLobby(nickname, lobbyID) {
    try {
        const response = await post("/send-join-lobby-data", {
            name: nickname,
            code: lobbyID
        }, 'application/json');
        
        get_lobby(response.url, response.code, response.lobby);
    } catch (error) {
        console.error(error);
        // You can re-throw the error if needed
        throw error;
    }    
}


async function sendNewCode(newCode) {
    try {
        const response = await post("/send-new-code", {
            userid: userID,
            code: newCode
        }, 'application/json');
        
        
    } catch (error) {
        console.error(error);
        // You can re-throw the error if needed
        throw error;
    }    
}
*/