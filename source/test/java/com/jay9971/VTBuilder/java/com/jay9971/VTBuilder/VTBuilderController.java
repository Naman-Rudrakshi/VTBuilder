package com.jay9971.VTBuilder;


import java.io.IOException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.util.Timer;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.Optional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.core.io.Resource;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import com.opencsv.CSVWriterBuilder;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.CsvToBeanFilter;



import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.jay9971.VTBuilder.Repository.ArchiveRepository;
import com.jay9971.VTBuilder.Repository.Users;
import com.jay9971.VTBuilder.Repository.UsersRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jay9971.VTBuilder.DataSchemas.*;
import com.jay9971.VTBuilder.Repository.Analytics;
import com.jay9971.VTBuilder.Repository.AnalyticsRepository;
import com.jay9971.VTBuilder.Repository.Archive;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.ClassRelativeResourceLoader;

import jakarta.servlet.http.HttpServletResponse;

@Controller
public class VTBuilderController {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	//private ImageManager img = new ImageManager();
	//private String image = img.getPath();
	
	@Autowired
	private ArchiveRepository archiveRepo;
	
	@Autowired
	private UsersRepository usersRepo;
	
	@Autowired
	private AnalyticsRepository analyticsRepo;
	
	
	private ArrayList<String> audio = new ArrayList<String>();
	
	
	/** Redirects to login page from default page **/
	@RequestMapping(value="", method=RequestMethod.GET)
	@ResponseBody
	public ModelAndView loginRedirect() {
	    ModelAndView modelAndView = new ModelAndView();
	    modelAndView.setViewName("redirect:/login");
	    return modelAndView;
	}
	
	
	/** Sends game HTML page**/
	@RequestMapping(value="/game", method=RequestMethod.GET)
	@ResponseBody
	public ModelAndView sendGamePage() {
	    ModelAndView modelAndView = new ModelAndView();
	    modelAndView.setViewName("gameplay_screen.html");
	    return modelAndView;
	}
	
	/** Sends game HTML page**/
	@RequestMapping(value="/dummy", method=RequestMethod.GET)
	@ResponseBody
	public ModelAndView sendDummy() {
	    ModelAndView modelAndView = new ModelAndView();
	    modelAndView.setViewName("dummy.html");
	    return modelAndView;
	}
	
	/** Sends post game HTML page**/
	@RequestMapping(value="/endgame", method=RequestMethod.GET)
	@ResponseBody
	public ModelAndView sendPostGamePage() {
	    ModelAndView modelAndView = new ModelAndView();
	    modelAndView.setViewName("post_game_screen.html");
	    return modelAndView;
	}
	
	
	/** Sends other static assets like CSS, JS, etc **/
	@RequestMapping(value="/staticPage", method = RequestMethod.GET)
	@ResponseBody
	public String redirect() {
		return "redirect:/static";
	}
	
	
	
	/** Sends the image to all clients**/
	@RequestMapping(value = "/get-image-from-server48281", method=RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
	public ResponseEntity<byte[]> sendImage(@RequestParam("userid") String data, HttpServletResponse response) throws IOException{
		Users user = usersRepo.findById((long)Integer.parseInt(data)).get();
		String image = archiveRepo.findById(user.getLobby()).get().getImage();
		
        Resource resource = new ClassPathResource(image);
        if (resource.exists()) {
            try (InputStream inputStream = resource.getInputStream()) {
                byte[] bytes = inputStream.readAllBytes();
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(bytes);
            }
        } else {
            // Handle image not found
            return ResponseEntity.notFound().build();
        }
    }
	
	
	
	
	
	
	@RequestMapping(value = "/send-analytics-data", method=RequestMethod.POST)
	public ResponseEntity<String> getAnalytics(@RequestBody String data){
		try {
			ObjectMapper objMapper = new ObjectMapper();
			JsonNode analytics = objMapper.readTree(data);
			
            int numberOfColumns = 1002;
            String[] newData = new String[numberOfColumns];
            newData[0] = analytics.get("gameNumber").asText();
            newData[1] = analytics.get("teamScore").asText();
            for (int i=2; i < 1000; i++) {
            	Integer num = i+1;
            	String str_convert = num.toString();
            	newData[i] = analytics.get(str_convert).asText();
            }

            //List<String[]> enter = new ArrayList<String[]>();
            //enter.add(newData);
            //Maker.writeCSVFile("analytics.csv", enter);
            
           
            BufferedWriter writer = new BufferedWriter(new FileWriter("analytics.csv", true));

            StringBuilder newDataLine = new StringBuilder();
            for (int i = 0; i < numberOfColumns; i++) {
                newDataLine.append(newData[i]);
                if (i < numberOfColumns - 1) {
                    newDataLine.append(",");
                } else {
                    newDataLine.append("\n");
                }
            }
            writer.write(newDataLine.toString());
            writer.close();

			
            
            List<String> command = new ArrayList<>();
            command.add("python"); 
            command.add("test.py");

            ProcessBuilder processBuilder = new ProcessBuilder(command);
            Process process = processBuilder.start();
			
			
			RequestConfirmationResponse dataObj = new RequestConfirmationResponse("confirmed");
		    String jsonData = objMapper.writeValueAsString(dataObj); 
		    HttpHeaders headers = new HttpHeaders();
		    headers.setContentType(MediaType.APPLICATION_JSON);

		    return new ResponseEntity<>(jsonData, headers, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	
	
	
	
	
	@RequestMapping(value = "/run-python-remote-test", method=RequestMethod.GET)
	public ResponseEntity<String> runPythonTest(@RequestParam("userid") String data, HttpServletResponse response) throws IOException{
		try {
			ObjectMapper objMapper = new ObjectMapper();
			
            
            List<String> command = new ArrayList<>();
            command.add("python"); 
            command.add("test.py");

            ProcessBuilder processBuilder = new ProcessBuilder(command);
            Process process = processBuilder.start();
            
            int exitCode = process.waitFor();
            
            logger.info("Python script executed with exit code: " + exitCode);
			
			
			RequestConfirmationResponse dataObj = new RequestConfirmationResponse("confirmed");
		    String jsonData = objMapper.writeValueAsString(dataObj); 
		    HttpHeaders headers = new HttpHeaders();
		    headers.setContentType(MediaType.APPLICATION_JSON);

		    return new ResponseEntity<>("" + exitCode, headers, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	
	
	
	
	
	
	
	@RequestMapping(value = "/get-audio", method=RequestMethod.POST)
	public ResponseEntity<String> sendAudio(@RequestBody String data, HttpServletResponse response) throws IOException{
		try {
			ObjectMapper objMapper = new ObjectMapper();
			LobbyRequestData rqData = objMapper.readValue(data, LobbyRequestData.class);
			Users user = usersRepo.findById((long)Integer.parseInt(rqData.getUserid())).get();
			Archive lobby = archiveRepo.findById(user.getLobby()).get();
			
			/*ArrayList<String> datas = new ArrayList<String>();
			for (long u=1; u <= usersRepo.count(); u++) {
				Users us = usersRepo.findById(u).get();
				
				if (us.getId() != user.getId() && user.getLobby() == us.getLobby()) {
					datas.add(us.getAudio());
				}
			}*/
			/*
			int totalPlayers = 0;
			for (long u=1; u <= usersRepo.count(); u++) {
				Users us = usersRepo.findById(u).get();
				if (us.getLobby() == lobby.getId()) {
					totalPlayers++;
				}
			}
			*/
			int totalPlayers = 0;
			for (long u=1; u <= usersRepo.findMaxId(); u++) {
				Users us;
				if (usersRepo.findById(u).isPresent()) {
					us = usersRepo.findById(u).get();
				} else {
					us = null;
				}
				if (us.getLobby() == lobby.getId()) {
					totalPlayers++;
				
				}
			}
			
			
			
			//String[] datas = new String[totalPlayers];
			ArrayList<String> datas = new ArrayList<String>();
			
			for (int i=0; i < audio.size(); i++) {
				
				
				if (usersRepo.findById((long)(i+1)).isPresent()) {
					Users us = usersRepo.findById((long)(i+1)).get(); 
					if (user.getId() != us.getId() && user.getLobby() == us.getLobby()) {
						datas.add(audio.get(i));
					}
				}
				
				
			}
			
			String[] send = new String[totalPlayers-1];
			for (int i=0; i < datas.size(); i++) {
				send[i] = datas.get(i);
			}
			
			
			AudioResponseData dataObj = new AudioResponseData(send);
			
		    String jsonData = objMapper.writeValueAsString(dataObj); 
		    HttpHeaders headers = new HttpHeaders();
		    headers.setContentType(MediaType.APPLICATION_JSON);
		    
		    return new ResponseEntity<>(jsonData, headers, HttpStatus.OK);
		    
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
        

    }
	
	
	
	
	
	
	@RequestMapping(value = "/send-audio", method=RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> receiveAudio(@RequestBody String data) throws IOException, URISyntaxException {
		try {
			ObjectMapper objMapper = new ObjectMapper();
			AudioRequestData rqData = objMapper.readValue(data, AudioRequestData.class);
			Users user = usersRepo.findById((long)Integer.parseInt(rqData.getUserid())).get();
			
			
			//user.setAudio(rqData.getAudioData());
			
			//usersRepo.save(user);
			
			audio.set((int)user.getId()-1, rqData.getAudioData());
			
		
			
			PostGameIntervalResponseData dataObj;
			dataObj = new PostGameIntervalResponseData("", "");
			
		    String jsonData = objMapper.writeValueAsString(dataObj); 
		    HttpHeaders headers = new HttpHeaders();
		    headers.setContentType(MediaType.APPLICATION_JSON);
		    
		    return new ResponseEntity<>(jsonData, headers, HttpStatus.OK);
		    
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	
	
		
	
	
	
	
	/*@RequestMapping(value = "/send-new-code", method=RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> setNewCode(@RequestBody String data) {
		try {
			ObjectMapper objMapper = new ObjectMapper();
			NewLobbyRequest rqData = objMapper.readValue(data, NewLobbyRequest.class);
			Users user = usersRepo.findById((long)Integer.parseInt(rqData.getUserid())).get();
			Archive lobby = archiveRepo.findById(user.getLobby()).get();
			
			lobby.setNewCode((long)Integer.parseInt(rqData.getCode()));
			archiveRepo.save(lobby);
			
			ReturnLobbyResponse dataObj = new ReturnLobbyResponse(null, null);
			
			
			
			String jsonData = objMapper.writeValueAsString(dataObj); 
		    HttpHeaders headers = new HttpHeaders();
		    headers.setContentType(MediaType.APPLICATION_JSON);
		    return new ResponseEntity<>(jsonData, headers, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}*/
	
	
	
	
	
	
	
	/** Sends initial data for post game**/
	/*
	@RequestMapping(value = "/return-to-lobby", method=RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> bootThem(@RequestBody String data) {
		try {
			ObjectMapper objMapper = new ObjectMapper();
			LobbyRequestData rqData = objMapper.readValue(data, LobbyRequestData.class);
			Users user = usersRepo.findById((long)Integer.parseInt(rqData.getUserid())).get();
			Archive lobby = archiveRepo.findById(user.getLobby()).get();
			
			lobby.setIsStarted(lobby.getIsStarted()+1);
			
			
			ReturnLobbyResponse dataObj = new ReturnLobbyResponse(user.getName(), "-1");
			
			// creates new lobby
			if (lobby.getNewCode() != 0) {
				dataObj.setLobbyCode(Integer.toString((int)lobby.getNewCode()));
			} else {
				lobby.setNewCode(archiveRepo.count() + 1001);
				
			}
			
	
			usersRepo.delete(user);
			
			archiveRepo.save(lobby);
			
			String jsonData = objMapper.writeValueAsString(dataObj); 
		    HttpHeaders headers = new HttpHeaders();
		    headers.setContentType(MediaType.APPLICATION_JSON);
		    return new ResponseEntity<>(jsonData, headers, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	*/
	
	
	
	
	
	
	/** Sends initial data for post game**/
	@RequestMapping(value = "/switch-to-lobby", method=RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> regresaAlLogin(@RequestBody String data) {
		try {
			ObjectMapper objMapper = new ObjectMapper();
			LobbyRequestData rqData = objMapper.readValue(data, LobbyRequestData.class);
			
			//Users user = usersRepo.findById((long)Integer.parseInt(rqData.getUserid())).get();
			//Archive lobby = archiveRepo.findById(user.getLobby()).get();
			
			
			/*if (lobby.getTimer() == -1) {
				Timer timer = new Timer();
				timer.schedule(new TimerTask() {
					  @Override
					  public void run() {
					    lobby.setTimer(timer);
					    archiveRepo.save(lobby);
					  }
					}, 1000);
			}*/
			
			
			//lobby.setTimer(-1);
			PostGameIntervalResponseData dataObj;
			dataObj = new PostGameIntervalResponseData("1", "");
			
			/*if (lobby.getTimer() == 0) {
				dataObj = new PostGameIntervalResponseData("1", "");
			} else {
				dataObj = new PostGameIntervalResponseData("0", null);
			}*/
			
			
		    String jsonData = objMapper.writeValueAsString(dataObj); 

		    HttpHeaders headers = new HttpHeaders();
		    headers.setContentType(MediaType.APPLICATION_JSON);
		    return new ResponseEntity<>(jsonData, headers, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	
	
	
	/** Sends initial data for post game**/
	@RequestMapping(value = "/send-post-game-initial-data", method=RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> postGameSendData(@RequestBody String data) {
		try {
			ObjectMapper objMapper = new ObjectMapper();
			LobbyRequestData rqData = objMapper.readValue(data, LobbyRequestData.class);
			logger.info("post zero");
			Users user = usersRepo.findById((long)Integer.parseInt(rqData.getUserid())).get();
			Archive lobby = archiveRepo.findById(user.getLobby()).get();
			Analytics repo = analyticsRepo.findById(user.getLobby()).get();
			
			//ADD TEAM SCORE< AND THE TIME TAKEN!!!!
			PostGameResponseData dataObj = new PostGameResponseData(lobby.getData(), repo.getTeamScore(), repo.getTimeElapsed());

			
			// this whole section is no longer relavant
			int uCount = 0;

			
			for (long u=1; u < usersRepo.count(); u++) {

				Users us = usersRepo.findById(u).get();

				if (us.getLobby() == lobby.getId()) {
					uCount++;
				}

			}

			
			lobby.setIsStarted(-uCount);
			
			
			archiveRepo.save(lobby);

		    String jsonData = objMapper.writeValueAsString(dataObj); 

		    
		    HttpHeaders headers = new HttpHeaders();
		    headers.setContentType(MediaType.APPLICATION_JSON);
		    logger.info("post three");
		    return new ResponseEntity<>(jsonData, headers, HttpStatus.OK);
		    
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	
	
	
	@RequestMapping(value = "/end-game", method=RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> recieveAnalytics(@RequestBody String data) {
		try {
			
			ObjectMapper objMapper = new ObjectMapper();
			EndGameRequestData rqData = objMapper.readValue(data, EndGameRequestData.class);
			
			Users user = usersRepo.findById((long)Integer.parseInt(rqData.getUserid())).get();
			Archive lobby = archiveRepo.findById(user.getLobby()).get();
			
			
			Analytics repo = analyticsRepo.findById((long)user.getLobby()).get();


			lobby.setIsStarted(2);
			repo.setTimeElapsed(rqData.getTimeElapsed());
			
			
		

			
			
			
			// get latest row in analytics.csv
			// get column 0 value (gameNumber - string)
			// 
			int latestVal = Maker.getNumberOfRows("analytics.csv");
			
			repo.setRo(latestVal);
			repo.setPostPlayers( (int) Math.sqrt(lobby.getData().length()/2));
			
            int numberOfColumns = 1002;
            int[] newData = new int[numberOfColumns];
            newData[0] = latestVal;
            for (int i=1; i < 1002; i++) {
            	newData[i] = 0;
            }
           
           
            //Maker.writeIntegersToCSV("analytics.csv", newData);
            
            BufferedWriter writer = new BufferedWriter(new FileWriter("analytics.csv", true));

            StringBuilder newDataLine = new StringBuilder();
            /*for (int i = 0; i < numberOfColumns; i++) {
                newDataLine.append(newData[i]);
                if (i < numberOfColumns - 1) {
                    newDataLine.append(",");
                } else {
                    newDataLine.append("\n");
                }
            }*/
            newDataLine.append(newData[0]);
            writer.write(newDataLine.toString());
            writer.close();
           
			
			archiveRepo.save(lobby);
			analyticsRepo.save(repo);	
			
			
			LobbyRequestData dataObj = new LobbyRequestData();
			dataObj.setUserid("");
			String jsonData = objMapper.writeValueAsString(dataObj);
			
		    HttpHeaders headers = new HttpHeaders();
		    headers.setContentType(MediaType.APPLICATION_JSON);
		    return new ResponseEntity<>(jsonData, headers, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	
	
	
	
	
	
	/** Receives the location data and adds it to database archive**/
	@RequestMapping(value = "/recieve-analytics", method=RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> sendAnalytics(@RequestBody String data) {
		try {
			ObjectMapper objMapper = new ObjectMapper();
			LobbyRequestData rqData = objMapper.readValue(data, LobbyRequestData.class);

			
			Users user = usersRepo.findById((long)Integer.parseInt(rqData.getUserid())).get();
			Analytics repo = analyticsRepo.findById((long)user.getLobby()).get();
			
			ArrayList<String> csvFileNames = new ArrayList<String>();
			csvFileNames.add("categorized_weights.csv");
			csvFileNames.add("impacts.csv");
			csvFileNames.add("category_counts.csv");
			csvFileNames.add("word_counts.csv");
			
			SendAnalyticsResponse dataObj;
			if (repo.getPostPlayers() == 0 && repo.getAiStatus() == 1) {
				dataObj = new SendAnalyticsResponse("1");
				/*for (int file=0; file < 4; file++) {
					for (int r=2; r < Maker.getNumberOfRows(csvFileNames.get(file)); r++) {
						if (repo.getRo() == r) {
							if (file == 0) {
								
								dataObj.setKey1(Maker.getCSVRowStr(csvFileNames.get(file), r*2));
								dataObj.setVal1(Maker.getCSVRowInt(csvFileNames.get(file), r*2+1));
							} else if (file == 1) {
								dataObj.setKey2(Maker.getCSVRowStr(csvFileNames.get(file), r*2));
								dataObj.setVal2(Maker.getCSVRowInt(csvFileNames.get(file), r*2+1));	
							} else if (file == 2) {
								dataObj.setKey3(Maker.getCSVRowStr(csvFileNames.get(file), r*2));
								dataObj.setVal3(Maker.getCSVRowInt(csvFileNames.get(file), r*2+1));	
							} else {
								dataObj.setKey4(Maker.getCSVRowStr(csvFileNames.get(file), r*2));
								dataObj.setVal4(Maker.getCSVRowInt(csvFileNames.get(file), r*2+1));	
							}
						}
					}
				}*/
				for (int file=0; file < 4; file++) {
					int r = repo.getRo();
					System.out.println("Row");
					System.out.println(r);
					if (file == 0) {
						System.out.println("category");
						dataObj.setKey1(Maker.getCSVRowStr(csvFileNames.get(file), r*2));
						dataObj.setVal1(Maker.getCSVRowInt(csvFileNames.get(file), r*2+1));
					} else if (file == 1) {
						System.out.println("impact");
						dataObj.setKey2(Maker.getCSVRowStr(csvFileNames.get(file), r*2));
						dataObj.setVal2(Maker.getCSVRowInt(csvFileNames.get(file), r*2+1));	
					} else if (file == 2) {
						System.out.println("third");
						dataObj.setKey3(Maker.getCSVRowStr(csvFileNames.get(file), r*2));
						dataObj.setVal3(Maker.getCSVRowInt(csvFileNames.get(file), r*2+1));	
					} else {
						System.out.println("fourth");
						String concatenatedString = String.join(" ", Maker.getCSVRowStr(csvFileNames.get(file), r*2));
						logger.info(concatenatedString);
						dataObj.setKey4(Maker.getCSVRowStr(csvFileNames.get(file), r*2));
						dataObj.setVal4(Maker.getCSVRowInt(csvFileNames.get(file), r*2+1));	
					}
						
					
				}
				logger.info("added all csv stuff");
			} else {
				dataObj = new SendAnalyticsResponse("n");
			}
			
			
			System.out.println(dataObj.toString());

			
			
			
			
			
			
		    String jsonData = objMapper.writeValueAsString(dataObj); 
		    HttpHeaders headers = new HttpHeaders();
		    headers.setContentType(MediaType.APPLICATION_JSON);
			
		    return new ResponseEntity<>(jsonData, headers, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	
	
	
	
	
	
	/** Receives the location data and adds it to database archive**/
	@RequestMapping(value = "/send-survey-data", method=RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> recieveSurveyData(@RequestBody String data) {
		try {
			
			ObjectMapper objMapper = new ObjectMapper();
			SurveyRequestData rqData = objMapper.readValue(data, SurveyRequestData.class);

			
			Users user = usersRepo.findById((long)Integer.parseInt(rqData.getUserid())).get();
			Analytics repo = analyticsRepo.findById((long)user.getLobby()).get();

			user.setSelfRating(rqData.getSelfRating());
			user.setTeamRating(rqData.getTeamRating());
			
			
			
            repo.setPostPlayers(repo.getPostPlayers()-1);
			
			if (repo.getPostPlayers() == 0) {
				logger.info("running ai");
				List<String> cmd = new ArrayList<String>();
				cmd.add("./vtenv/bin/python3");
				cmd.add("ai.py");
				cmd.add("run_script");
				cmd.add("" + repo.getRo());
	            ProcessBuilder processBuilder = new ProcessBuilder(cmd);
	            
	            processBuilder.redirectErrorStream(true);

	            Process process = processBuilder.start();
	            
	            InputStream combinedOutput = process.getInputStream();
	            BufferedReader reader = new BufferedReader(new InputStreamReader(combinedOutput));

	            String line;
	            while ((line = reader.readLine()) != null) {
	                System.out.println(line);
	            }
	            
	            int exitCode = process.waitFor();
	            Integer ex = exitCode;
				logger.info(ex.toString());
				
				repo.setAiStatus(1);
			}
			
			
			
			usersRepo.save(user);
			
			SurveyRequestData dataObj = new SurveyRequestData();
			dataObj.setUserid("ok");
			String jsonData = objMapper.writeValueAsString(dataObj);
			
		    HttpHeaders headers = new HttpHeaders();
		    headers.setContentType(MediaType.APPLICATION_JSON);
		    return new ResponseEntity<>(jsonData, headers, HttpStatus.OK);
			
			
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	
	
	
	
	/** Receives the location data and adds it to database archive**/
	@RequestMapping(value = "/send-post-game-transfer-data", method=RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> postGameTransfer(@RequestBody String data) {
		try {

			ObjectMapper objMapper = new ObjectMapper();
			
			SquareRequestData rqData = objMapper.readValue(data, SquareRequestData.class);

			Users user = usersRepo.findById((long)Integer.parseInt(rqData.getUserid())).get();
			Archive lobby = archiveRepo.findById(user.getLobby()).get();
			Analytics repo = analyticsRepo.findById((long)user.getLobby()).get();
			
			user.setNumPlacements(rqData.getNumPlacements());
			user.setPlayerAccuracy(rqData.getPlayerAccuracy());

			
			//handles analytics data
			
			//change to max ID
			double totalAccs = 0.0;
			int c = 0;
			for (long u=1; u <= usersRepo.findMaxId(); u++) {
				Users us;
				if (usersRepo.findById(u).isPresent()) {
					us = usersRepo.findById(u).get();
				} else {
					us = null;
				}
				
				if ( us != null && us.getLobby() == lobby.getId()) {
					totalAccs += us.getPlayerAccuracy();
					c++;
				}
			}

			repo.setNumOfPlayers(c);

			double teamScore = totalAccs/c;
			Double toLog = teamScore;
			logger.info(toLog.toString());
			logger.info("c: " + c);
			DecimalFormat df = new DecimalFormat("0.00"); 
			String formatTeamScore = df.format(teamScore);
			repo.setTeamScore(formatTeamScore);
			
			
			
			String keyString = Arrays.toString(rqData.getKeyList());
			String valString = Arrays.toString(rqData.getValueList());
			Integer row = repo.getRo();
			String rowString = row.toString();
			String accString = repo.getTeamScore();
			
            
			List<String> command = new ArrayList<>();
			command.add("./vtenv/bin/python3");
            command.add("csv_updater.py");
            command.add(keyString);
            command.add(valString);
            command.add(rowString);
            command.add(accString);
            logger.info("tran4");

            ProcessBuilder processBuilder = new ProcessBuilder(command);
            logger.info("tran5");
            processBuilder.redirectErrorStream(true);
            logger.info("tran6");
            
            try {
                Process process = processBuilder.start();

                // Create a reader to read the process's standard output
                InputStream combinedOutput = process.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(combinedOutput));
                String line;
                while ((line = reader.readLine()) != null) {
                    // Print or process the output from the Python script
                    System.out.println(line);
                }

                int exitCode = process.waitFor();
                Integer ex = exitCode;
    			
                logger.info("tran10");
                logger.info(ex.toString());
            } catch (IOException e) {
                // Log the IOException and its message
                e.printStackTrace();
            } catch (InterruptedException e) {
                // Log the InterruptedException and its message
                e.printStackTrace();
            }
            
            
            //  /Users/Vidyamruth/Downloads/VTBuilder-final.jay 2/vtenv

			
			
            
			
			
			// handles sqaure position data
			
			String dat = lobby.getData();
			//String[] dat = datas[lobby.getId() - 1000];
			
			ArrayList<String> dat_wrapper = new ArrayList<String>();
			for (int i=0; i < dat.length(); i += 2) {
				dat_wrapper.add(dat.substring(i, i+2));
			}
			
			

			String inData = rqData.getOccupiedSquares();
			logger.info("recieved occ " + inData);
			
			ArrayList<String> dat_from_user = new ArrayList<String>();
			for (int i=0; i < inData.length(); i += 2) {
				dat_from_user.add(inData.substring(i, i+2));
			}
			
			
			for (int i=0; i < dat_from_user.size(); i++) {
				String slot = dat_from_user.get(i);
				if (slot.charAt(1) != 'x') {
					dat_wrapper.set(i, slot);
				}
			}
			
			
			String finalData = "";
			for (String datSlot : dat_wrapper) {
				finalData += datSlot;
			}
			
			logger.info("final data");
			logger.info(finalData);
			
			lobby.setData(finalData);
			archiveRepo.save(lobby);
			usersRepo.save(user);
			analyticsRepo.save(repo);
			

			
			PostGameUrlData dataObj = new PostGameUrlData("/endgame");
			String jsonData = objMapper.writeValueAsString(dataObj);

			
		    HttpHeaders headers = new HttpHeaders();
		    headers.setContentType(MediaType.APPLICATION_JSON);

		    return new ResponseEntity<>(jsonData, headers, HttpStatus.OK);
		    
		  
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	

	
	/** Receives the location data and adds it to database archive**/
	@RequestMapping(value = "/send-square-locations-data", method=RequestMethod.POST)
	@ResponseBody
	public void receiveData(@RequestBody String data) {
		try {
			ObjectMapper objMapper = new ObjectMapper();
			SquareRequestData rqData = objMapper.readValue(data, SquareRequestData.class);
			
			Users user = usersRepo.findById((long)Integer.parseInt(rqData.getUserid())).get();
			Archive lobby = archiveRepo.findById(user.getLobby()).get();
			
			lobby.setData(rqData.getOccupiedSquares());
			logger.info("Occupied Squares: " + rqData.getOccupiedSquares());
			archiveRepo.save(lobby);
			
		} catch (Exception e) {
			
		}
	} 
	
	
	/** Sends the location data out to every client (should be always active) **/
	@RequestMapping(value = "/get-square-locations-data", method=RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> sendData(@RequestBody String data) {
		try {
			ObjectMapper objMapper = new ObjectMapper();
			LobbyRequestData rqData = objMapper.readValue(data, LobbyRequestData.class);
			
			Users user = usersRepo.findById((long)Integer.parseInt(rqData.getUserid())).get();
			Archive lobby = archiveRepo.findById(user.getLobby()).get();
			ArrayList<String> players = new ArrayList<String>();
			for (long u=1; u <= usersRepo.findMaxId(); u++) {
				Users us;
				if (usersRepo.findById(u).isPresent()) {
					us = usersRepo.findById(u).get();
				} else {
					us = null;
				}
				if (us.getLobby() == lobby.getId() && us.getId() != user.getId()) {
					players.add(us.getName());
				} else if (us.getId() == user.getId()) {
					players.add(0, us.getName());
				}
			}
			String player_list = "";
			for (String player : players) {
				player_list += player;
			}
			SquareResponseData dataObj = new SquareResponseData(lobby.getData(), Integer.toString(lobby.getIsStarted()), player_list);
			
		    String jsonData = objMapper.writeValueAsString(dataObj);
		    
		    HttpHeaders headers = new HttpHeaders();
		    headers.setContentType(MediaType.APPLICATION_JSON);
		    return new ResponseEntity<>(jsonData, headers, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	
	
	
	
	
	
	
	@RequestMapping(value = "/get-game-initial-data", method=RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> gameStarter(@RequestBody String data) {
		try {
			ObjectMapper objMapper = new ObjectMapper();
			LobbyRequestData rqData = objMapper.readValue(data, LobbyRequestData.class);
			
			Users user = usersRepo.findById((long)Integer.parseInt(rqData.getUserid())).get();
			Archive lobby = archiveRepo.findById(user.getLobby()).get();
			
			StartGameResponseData dataObj = new StartGameResponseData();
			
			int total_players = 0;
			for (long userID = 1; userID <= usersRepo.count(); userID++) {
				Users us = usersRepo.findById(userID).get();
				if (us != null && us.getLobby() == lobby.getId()) {
					total_players++;
				}
			}
			

			int squares_left = lobby.getSquares();
			
			String squares = lobby.getData();
			int total_squares = squares.length()/2;
			
			
			ArrayList<String> list_squares = new ArrayList<String>();
			for (int i=0; i < squares.length(); i+=2) {
				list_squares.add(squares.substring(i, i+2));
			}
			
			int count = 0;
			String player_squares = "";
			while (count < (int)(total_squares/total_players)) {
				int rand = (int)(Math.random() * total_squares);
				if (list_squares.get(rand).charAt(1) != 'F') {
					player_squares += list_squares.get(rand);
					count++;
					squares_left--;
					list_squares.set(rand, "FF");
				}
			}
			
			squares = "";
			for (String square : list_squares) {
				squares += square;
			}
			
			
			dataObj.setAssignedSquares(player_squares);
			dataObj.setImageUrl("/get-image-from-server48281");
			dataObj.setNumSquares(Integer.toString(total_squares));
			
			
			if (squares_left == 0) {
				squares = "";
				for (int i=0; i < total_squares; i++) {
					squares += "00";
				}
			}
			
			lobby.setData(squares);
			lobby.setSquares(squares_left);
			
			archiveRepo.save(lobby);
			
			
		    String jsonData = objMapper.writeValueAsString(dataObj);
		    

		    HttpHeaders headers = new HttpHeaders();
		    headers.setContentType(MediaType.APPLICATION_JSON);
		    return new ResponseEntity<>(jsonData, headers, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	
	
	
	
	
	
	@RequestMapping(value = "/leave-game", method=RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> leaveGame(@RequestBody String data) {
		try {
			ObjectMapper objMapper = new ObjectMapper();
			LobbyRequestData rqData = objMapper.readValue(data, LobbyRequestData.class);
			
			Users user = usersRepo.findById((long)Integer.parseInt(rqData.getUserid())).get();
			Archive lobby = archiveRepo.findById(user.getLobby()).get();
			
			
			usersRepo.delete(user);
			
			int player_total = 0;
			if (usersRepo.findMaxId() == null) {
				archiveRepo.delete(lobby);
			} else {
				for (long u=1; u <= usersRepo.findMaxId(); u++) {
					logger.info("GOT HERE???");
					if (usersRepo.findById(u).isPresent() && usersRepo.findById(u).get().getLobby() == user.getLobby()) {
						logger.info("got here bruh");
						player_total++;
					}
				}
				logger.info(Integer.toString(player_total));
				if (player_total == 0) {
					archiveRepo.delete(lobby);
				}
			}
			
			LobbyRequestData response = new LobbyRequestData("function_code:0C9472F");
			
		    String jsonData = objMapper.writeValueAsString(response);
		    
	
		    
		    HttpHeaders headers = new HttpHeaders();
		    headers.setContentType(MediaType.APPLICATION_JSON);
		    return new ResponseEntity<>(jsonData, headers, HttpStatus.OK);
			
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}
	
	
	
	
	
	
	@RequestMapping(value = "/start-game", method=RequestMethod.POST)
	@ResponseBody
	public void startGame(@RequestBody String data) {
		try {
			
			ObjectMapper objMapper = new ObjectMapper();
			LobbyRequestData rqData = objMapper.readValue(data, LobbyRequestData.class);
			
			Archive lobby = archiveRepo.findById(usersRepo.findById((long)Integer.parseInt(rqData.getUserid())).get().getLobby()).get();
			
			//if server is already started
			if (lobby.getIsStarted() == 1) {
				return;
			}
			
			if (lobby.getIsStarted() == 0) {
				lobby.setIsStarted(1);
			}
			
			
			ArrayList<Users> players = new ArrayList<Users>();
			for (long userID = 1; userID <= usersRepo.count(); userID++) {
				Users user = usersRepo.findById(userID).get();
				if (user != null && user.getLobby() == lobby.getId()) {
					players.add(user);
				}
			}
			// calculate squares, distribution of players, get image
			int total_players = players.size();
			int total_squares = total_players * total_players;
			
			lobby.setSquares(total_squares);
			
			String squareData = "";
			for (int i=0; i < total_squares; i++) {
				squareData += Maker.serialize(i);
			}
			
			
			lobby.setData(squareData);

			
			ImageManager imgManager = new ImageManager();
			String image = imgManager.getPath();
			
			
			lobby.setImage(image);
			
			for (Users player: players) {
				player.setSquares(squareData);
			}
			
			
			for (Users player: players) {
				usersRepo.save(player);
			}
			
			archiveRepo.save(lobby);
			

		} catch (Exception e) {
			
		}
	}
	
	
	
	
	
	
	
	@RequestMapping(value = "/get-player-list-in-lobby", method=RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> sendPlayerList(@RequestBody String data) {
		try {
			ObjectMapper objMapper = new ObjectMapper();
			LobbyRequestData rqData = objMapper.readValue(data, LobbyRequestData.class);
			String userid = rqData.getUserid();
			//logger.info("player 1");
			Users current_user = usersRepo.findById((long)Integer.parseInt(userid)).get();
			
			ArrayList<String> arr = new ArrayList<String>();
			//logger.info("player 1.1");
			for (long userID = 1; userID <= usersRepo.findMaxId(); userID++) {
			//	logger.info("player 1.2");
				Optional<Users> user_wrapper = usersRepo.findById(userID); 
			//	logger.info("player 1.3");
				Users user;
				if (user_wrapper.isPresent()) {
					user = user_wrapper.get();
				} else {
					user = null;
				}
				
			//	logger.info("player 2");
				if (user != null && user.getId() == current_user.getId()) {
					arr.add(0, user.getName());
				} 
				else if (user != null && user.getLobby() == current_user.getLobby()){
					arr.add(user.getName());	
				}
			}	
			
			Archive lobby = archiveRepo.findById(current_user.getLobby()).get();
			String final_list = "";
			for (int i=0; i < arr.size(); i++) {
				final_list += arr.get(i);
			}
			//logger.info("player 3");
		
			LobbyResponseData response = new LobbyResponseData();
			response.setUrl("/game");
			response.setIsStarted(Integer.toString(lobby.getIsStarted()));
			response.setUserList(final_list);
			//response.setUrl("/game");
			//response.setIsStarted("0");
			//response.setUserList("userList");
			//logger.info("player 4");

		    String jsonData = objMapper.writeValueAsString(response);
		    
		    HttpHeaders headers = new HttpHeaders();
		    headers.setContentType(MediaType.APPLICATION_JSON);
		    return new ResponseEntity<>(jsonData, headers, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		
	}
	
	
	
	
	
	
	
	@RequestMapping(value="/lobby", method=RequestMethod.GET)
	@ResponseBody
    public ModelAndView lobby(@RequestParam("userid") String userid) {
	    ModelAndView modelAndView = new ModelAndView();
	    modelAndView.setViewName("lobby.html");
        modelAndView.addObject("userid", userid);
	    modelAndView.addObject("lobby", usersRepo.findById((long)Integer.parseInt(userid)).get().getLobby());
	    return modelAndView;
	}
	
	
	

	
	@RequestMapping(value="/login", method=RequestMethod.GET)
	@ResponseBody
	public ModelAndView login() {
		
	    ModelAndView modelAndView = new ModelAndView();
	    modelAndView.setViewName("login.html");
	    
	    return modelAndView;
	}
	
	
	
	/** Login create lobby request mapping **/
	@RequestMapping(value="/create-lobby", method=RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> createLobby(@RequestBody String data) {
		try {
			
			ObjectMapper objMapper = new ObjectMapper();
			CreateLobbyRequestData rqData = objMapper.readValue(data, CreateLobbyRequestData.class);
			
			//Generate new lobby + code
			long lobbyCode = archiveRepo.count() + 1000;
			archiveRepo.save(new Archive(lobbyCode, "image-url", "", -1, 0));
			
			//Create analytics repo for lobby
			analyticsRepo.save(new Analytics(lobbyCode));
			
			//Generate new user + userid
			long total_users = usersRepo.count();
			long newID = total_users + 1;
			Users new_user = new Users(newID, rqData.getName(), "", lobbyCode);
			usersRepo.save(new_user);
			
			audio.add("");
			
			//response handling
		    LoginResponseData dataObj = new LoginResponseData();
		    dataObj.setCode(Maker.serialize((int)(newID)));
		    dataObj.setUrl("lobby");
		    dataObj.setLobby(Integer.toString((int)lobbyCode));
		    
		    String jsonData = objMapper.writeValueAsString(dataObj);
		    
		    HttpHeaders headers = new HttpHeaders();
		    headers.setContentType(MediaType.APPLICATION_JSON);
		    return new ResponseEntity<>(jsonData, headers, HttpStatus.OK);
		    
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	
	
	
	/** Login join lobby request mapping **/
	@RequestMapping(value="/send-join-lobby-data", method=RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> recieveLobbyCode(@RequestBody String data) {
		try {
			ObjectMapper objMapper = new ObjectMapper();
			LoginRequestData rqData = objMapper.readValue(data, LoginRequestData.class);
			
			// sends a fuck off request if the game is active
			if (archiveRepo.findById((long)Integer.parseInt(rqData.getCode())).get().getIsStarted() == 1) {
				LoginResponseData dataObj = new LoginResponseData();
				dataObj.setCode("");
				dataObj.setUrl("Fuck off");
				dataObj.setLobby("");
				String jsonData = objMapper.writeValueAsString(dataObj);
			    HttpHeaders headers = new HttpHeaders();
			    headers.setContentType(MediaType.APPLICATION_JSON);
			    return new ResponseEntity<>(jsonData, headers, HttpStatus.OK);
			}
		
			// calculates new user id based on past users and creates a new user entry in the repo
			long total_users = usersRepo.count();
			long newID = total_users + 1;
			Users new_user = new Users(newID, rqData.getName(), "", (long)Integer.parseInt(rqData.getCode()));
			usersRepo.save(new_user);
			
			audio.add("");
			
			// response handling
		    LoginResponseData dataObj = new LoginResponseData();
		    dataObj.setCode(Maker.serialize((int)(newID)));
		  
		    dataObj.setUrl("lobby");
		    dataObj.setLobby(rqData.getCode());
		    
		    String jsonData = objMapper.writeValueAsString(dataObj);
		    
		    HttpHeaders headers = new HttpHeaders();
		    headers.setContentType(MediaType.APPLICATION_JSON);
		    return new ResponseEntity<>(jsonData, headers, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
}


