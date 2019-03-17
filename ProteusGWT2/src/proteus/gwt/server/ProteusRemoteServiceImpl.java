package proteus.gwt.server;

import java.io.BufferedReader;
import java.io.CharArrayReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletException;

import proteus.gwt.client.app.AppController;
import proteus.gwt.client.app.ui.util.MiscUtils;
import proteus.gwt.client.proxy.ProteusRemoteService;
import proteus.gwt.server.businesslogic.ComponentNameProxy;
import proteus.gwt.server.businesslogic.DomainComponentProxy;
import proteus.gwt.server.businesslogic.DomainModelProxy;
import proteus.gwt.server.businesslogic.DomainModelProxy2;
import proteus.gwt.server.businesslogic.ExportProteusProxy;
import proteus.gwt.server.businesslogic.PersistenceManagerGWT;
import proteus.gwt.server.businesslogic.ProteuswebProxy;
import proteus.gwt.server.db.MOFileParser;
import proteus.gwt.server.domain.Content_type_mod;
import proteus.gwt.server.domain.Files;
import proteus.gwt.server.domain.Users;
import proteus.gwt.server.exceptions.ComponentNotFoundException;
import proteus.gwt.server.exceptions.ModelNotFoundException;
import proteus.gwt.server.modelica.omc.ConnectException;
import proteus.gwt.server.modelica.omc.OMCProxy;
import proteus.gwt.server.util.ParseException;
import proteus.gwt.server.util.PltExtHelper;
import proteus.gwt.server.util.ServerContext;
import proteus.gwt.shared.datatransferobjects.ComponentDTO;
import proteus.gwt.shared.datatransferobjects.ExportProteusDTO;
import proteus.gwt.shared.datatransferobjects.ExportProteusResponseDTO;
import proteus.gwt.shared.datatransferobjects.GetClassNamesResponseDTO;
import proteus.gwt.shared.datatransferobjects.GetModelResponseDTO;
import proteus.gwt.shared.datatransferobjects.ModelDTO;
import proteus.gwt.shared.datatransferobjects.PlotDataDTO;
import proteus.gwt.shared.datatransferobjects.SimulationResultDTO;
import proteus.gwt.shared.types.Constants;
import proteus.gwt.shared.util.Utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.LongSerializationPolicy;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class ProteusRemoteServiceImpl extends RemoteServiceServlet implements
		ProteusRemoteService, Constants {

	private static final long serialVersionUID = 1L;

	private GetClassNamesResponseDTO classNamesResponse;
	private HashMap<String, ComponentDTO> componentDTOMap = new HashMap<String, ComponentDTO>();
	private String componentNameXML;

	private File workDir;
	private final static String DEVELOPDIR = "C:\\tmp";
	private OMCProxy omcProxy;

	@Override
	public String getUploadUrl() {
		String ret = "";
		ret = ServerContext.get("MoParserURL") + "/uploadmofile";
		System.out.println("uploadurl in server: " + ret);
		return ret;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * proteus.gwt.client.proxy.ProteusRemoteService#getModel(java.lang.Long)
	 * this is the traditional way of getModel object deperacted, use
	 * getModelJSOn instead
	 */
	@Override
	@Deprecated
	public GetModelResponseDTO getModel(Long modelId) {
		GetModelResponseDTO response = new GetModelResponseDTO();

		response.setModelID(modelId);
		// response.setSuccess(true);
		try {
			ComponentDTO componentDTO = DomainModelProxy.getModel(modelId);
			response.setComponentDTO(componentDTO);
			response.setSuccess(true);
			response.setMessage("model get successful");
		} catch (ModelNotFoundException e) {
			response.setSuccess(false);
			response.setMessage("model not found");
		} catch (ComponentNotFoundException e) {
			response.setSuccess(false);
			response.setMessage("component not found");
		}

		return response;
	}

	@Override
	public GetClassNamesResponseDTO getClassNames() {
		// if(!response.isSuccess()){
		// init();
		// }
		return classNamesResponse;
	}

	@Override
	public String getComponentNameXML() {
		if (Utils.DEBUGUI)
			System.out.println("Get ComponentNameXML is invoked!");
		return componentNameXML;
	}

	@Override
	public void init() {
		if (Utils.DEBUGUI)
			System.out.println("start init() in ProteusRemoteServiceImpl");
		// initClassNameList();
		if (!AppController.JSON_FLAG) {
			initComponent();
		}
//		 ComponentNameProxy.writeComponentToXMLFile();
		initComponentNameXML();

//		delOldTempObj();
//		 if (!ServerContext.isLocal())
		initOMCServer();

		if (Utils.DEBUG)
			System.out.println("end init() in ProteusRemoteServiceImpl");
	}

	private void delOldTempObj() {
		// TODO Auto-generated method stub
		//@@@should kill old cmd process??
		String temp = System.getenv("TMP");
		String tempDir = System.getProperty("java.io.tmpdirs");
		String tempFile = tempDir + "openmodelica.objid.ECLIPSE";
		System.err.println(tempFile);
		File f = new File(tempFile);
		System.out.println(f.delete());
	}

	private void initComponentNameXML() {
		String componentFilesPath = ServerContext.get("ProteusRootPath")
				+ ServerContext.get("ProteusFilesPath") + "/componentName.xml";

		FileReader fr;
		BufferedReader br;
		String strLine = "";
		String strAll = "";
		try {
			fr = new FileReader(componentFilesPath);
			br = new BufferedReader(fr);
			while ((strLine = br.readLine()) != null) {
				strAll += strLine;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		componentNameXML = strAll;
	}

	private void initOMCServer() {
		Enumeration<String> paramNames = getServletConfig()
				.getInitParameterNames();
		System.out.println("param names");
		while (paramNames.hasMoreElements()) {
			System.out.println(paramNames.nextElement());
		}
		String wdpath = getServletConfig().getInitParameter("WorkDir");
		
		
		if (wdpath == null) {
			wdpath = DEVELOPDIR;
		}
		workDir = new File(wdpath);
		if (!workDir.exists()) {
			workDir.mkdirs();
		}

		omcProxy = new OMCProxy();
		// Comment out to prevent loading library
		// *
		try { // Load library
			omcProxy.getStandardLibrary();

			// Change to workDir
			omcProxy.sendExpression("cd(\""
					+ workDir.getAbsolutePath().replace("\\", "/") + "\")");
		} catch (ConnectException e) {
			try {
				throw new ServletException(e);
			} catch (ServletException e1) {
				e1.printStackTrace();
			}
		}
		// */

	}

	private void initClassNameList() {
		// ArrayList<String> nameList = ComponentNameProxy.getNames();
		// TreeData treeData = new TreeData();
		// treeData.insertTo(nameList);
		//
		// classNamesResponse = new GetClassNamesResponseDTO();
		// classNamesResponse.setMessage("success");
		// classNamesResponse.setSuccess(true);
		//
		// classNamesResponse.setTreeData(treeData);
	}

	public void initComponent() {
		String componentFilesPath = ServerContext.get("ProteusRootPath")
				+ ServerContext.get("ProteusFilesPath") + "/ComponentList.txt";

		// EntityManagerFactory emf = Persistence
		// .createEntityManagerFactory("ProteusGWT");
		// EntityManager em = emf.createEntityManager();
		EntityManager em = PersistenceManagerGWT.get().getEntityManager();
		try {
			FileReader fr;
			BufferedReader br;
			String wholeName = "";

			fr = new FileReader(componentFilesPath);
			br = new BufferedReader(fr);
			while ((wholeName = br.readLine()) != null) {
				try {
					// System.out.println(wholeName);
					ComponentDTO componentDTO = DomainComponentProxy
							.getComponent(wholeName, em);
					componentDTOMap.put(wholeName, componentDTO);
				} catch (ComponentNotFoundException e) {
					continue;
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		em.close();
		// emf.close();
	}

	@Override
	public ComponentDTO getDemo(String name) {
		try {
			return DomainModelProxy2.getComponentModel(name);
		} catch (ModelNotFoundException e) {
			System.err.println("Component not Found");
		} catch (ComponentNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public SimulationResultDTO Simulate(String resp) {
		if (resp == null || resp.length() == 0)
			return null;
		if (omcProxy != null) {
			omcProxy.emptyMsgQ();
		}

		try {
			return sendJob(resp);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private SimulationResultDTO getFakeResult() throws IOException,
			ParseException {
		SimulationResultDTO simulate = new SimulationResultDTO();
		String resultFilesPath = ServerContext.get("ProteusRootPath")
				+ ServerContext.get("ProteusFilesPath") + "/MyModel_res.plt";
		File f = new File(resultFilesPath);
		PlotDataDTO[] pds = PltExtHelper.readPltPlot(f, simulate.getPds());
		return simulate;
	}

	private SimulationResultDTO sendJob(String resp) throws IOException {
		BufferedReader r = getBufferReader(resp);

		String model = ExtractInfo(r, HEADER_PREFIX_MODEL);
		String startT = ExtractInfo(r, HEADER_PREFIX_START_TIME);
		double startTime = Utils.parseDouble(startT);
		String stopT = ExtractInfo(r, HEADER_PREFIX_STOP_TIME);
		double stopTime = Utils.parseDouble(stopT);
		String intervalT = ExtractInfo(r, HEADER_PREFIX_INTERVALS);
		int numOfInterval = Utils.parseInteger(intervalT);
		// int numOfInterval = Utils.parseDouble(s);
		File file = writeMOfile(r);
		// File file = new File("D:/1.mo");
		return RunSimulation(model, startTime, stopTime, numOfInterval, file);
	}

	private SimulationResultDTO runMSLModel(String model, double startTime,
			double stopTime) {
		SimulationResultDTO simDTO = new SimulationResultDTO();
		// load the stand msl library if not
		try {
			if(omcProxy==null)
				System.err.println("omcProxy null, initilize required");
			omcProxy.getStandardLibrary();
			String exist = omcProxy.existModel(model);
			if (exist != null && exist.equalsIgnoreCase("true")) {
				String result = omcProxy.sendExpression("simulate(" + model
						+ ", startTime=" + startTime + ", stopTime=" + stopTime
						+ ", outputFormat=\"plt\")");
				File f = new File(workDir + "/" + model + "_res.plt");
				PltExtHelper.readPltPlot(f, simDTO.getPds());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return simDTO;
	}

	private void tempini() {
		String wdpath;
		wdpath = DEVELOPDIR;

		workDir = new File(wdpath);
		if (!workDir.exists()) {
			workDir.mkdirs();
		}

		omcProxy = new OMCProxy();
		try { // Load library
			omcProxy.getStandardLibrary();
			omcProxy.sendExpression("cd(\""
					+ workDir.getAbsolutePath().replace("\\", "/") + "\")");
		} catch (ConnectException e) {
			try {
				throw new ServletException(e);
			} catch (ServletException e1) {
				e1.printStackTrace();
			}
		}
	}

	
	private SimulationResultDTO RunSimulation(String model, double startTime,
			double stopTime, int numOfInterval, File file) {
		SimulationResultDTO simDTO = new SimulationResultDTO();
		try {
			log("current dir = " + omcProxy.sendExpression("cd()"));

			log("Loading source file...");
			omcProxy.loadSourceFile(file.getAbsolutePath().replace("\\", "/"));
			log(" Done\n");
			log("Simulating model (" + model + ")...");
			String result = omcProxy.sendExpression("simulate(" + model
					+ ", startTime=" + startTime + ", stopTime=" + stopTime
					+ ", numberOfIntervals=" + numOfInterval
					+ ", outputFormat=\"plt\")");
			log(" Done\n");
			log("=================\n");
			log("Compiler message:\n");
			log("=================\n");
			log(result + "\n");

			File f = new File(workDir + "/" + model + "_res.plt");
			PlotDataDTO[] pds = PltExtHelper.readPltPlot(f, simDTO.getPds());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return simDTO;
	}

	private File writeMOfile(BufferedReader r) {
		// Read content
		String id = randomAlphaNum(5);
		File file = new File(workDir + "/" + id + ".mo");
		try {
			PrintWriter w = new PrintWriter(new FileWriter(file));
			String line;
			while ((line = r.readLine()) != null) {
				w.println(line);
			}
			w.flush();
			w.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return file;
	}

	private String ExtractInfo(BufferedReader r, String prefix) {
		try {
			String line = r.readLine();
			if (line == null || !line.startsWith(prefix)) {
				sendError(prefix);
				return null;
			}
			return line.substring(prefix.length()).trim();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private BufferedReader getBufferReader(String resp) {
		char buf[] = new char[resp.length()];
		resp.getChars(0, resp.length(), buf, 0);
		CharArrayReader in = new CharArrayReader(buf);
		BufferedReader r = new BufferedReader(in);
		return r;
	}

	private void runMultibody(String model, File file) {
		// boolean stop = false;
		// File plotFile = new File(workDir + "/" + model + "_res.plt");
		// try {
		// String[] cmd = new String[6];
		// cmd[0] = "wscript.exe";
		// cmd[1] = "../webapps/proteus/simx.vbs";
		// cmd[2] = "//I";
		// cmd[3] = model;
		// cmd[4] = file.getAbsolutePath().replace("\\", "/");
		// cmd[5] = plotFile.getAbsolutePath().replace("\\", "/");
		// Process proc = Runtime.getRuntime().exec(cmd);
		// // proc.waitFor();
		// while (!stop) {
		// try {
		// proc.exitValue();
		// } catch (IllegalThreadStateException e) { // not
		// // finished
		// Thread.sleep(500);
		// continue;
		// }
		// break; // break if finished
		// }
		// if (stop) {
		// proc.destroy();
		// }
		// } catch (Exception e) {
		// StringBuilder errorStr = new StringBuilder();
		// errorStr.append("\nERROR:\n");
		// errorStr.append(e.getMessage() + "\n");
		// log(errorStr.toString());
		// current.statusCode = RunInfo.STATUS_ERROR;
		// return;
		// }
		//
		// if (stop) {
		// log("Simulation Stopped.");
		// return;
		// }
		// String result = "";
		// log("=================\n");
		// log("Compiler message:\n");
		// log("=================\n");
		// log(result + "compiler message is not supported yet\n");
		// if (stop) {
		// log("Simulation Stopped.");
		// current.statusCode = RunInfo.STATUS_CANCELED;
		// return;
		// }
		// System.out.println("WorkDir = " + workDir);
		// System.out.println("Plot file: " + plotFile);
		//
		// if (!plotFile.exists()) {
		// log("Can't find plot: " + model + ", won't be able to plot\n");
		// return;
		// }
		// current.plotFile = plotFile;
		// current.statusCode = RunInfo.STATUS_DONE;
	}

	private static String randomAlphaNum(int length) {
		byte[] buf = new byte[length];
		for (int i = 0; i < buf.length; i++) {
			int n = (int) (Math.random() * 62);
			if (n < 10) {
				buf[i] = (byte) (0x30 + n);
			} else if (n < 36) {
				n -= 10;
				buf[i] = (byte) (0x41 + n);
			} else {
				n -= 36;
				buf[i] = (byte) (0x61 + n);
			}
		}
		return new String(buf);
	}

	private void sendError(String resp) {

	}

	@Override
	public ExportProteusResponseDTO exportProteus(ExportProteusDTO exportDTO) {
		ExportProteusResponseDTO response = new ExportProteusResponseDTO();
		String path = "";
		try {
			switch (exportDTO.getType()) {
			case BMP:
				path = ExportProteusProxy.saveImage(exportDTO, "bmp");
				response.setUrl(path);
				response.setSuccess(true);
				System.out.println("proteus png:\t" + response.getUrl());
				break;
			case JPEG:
				path = ExportProteusProxy.saveImage(exportDTO, "jpeg");
				response.setUrl(path);
				response.setSuccess(true);
				break;
			case PNG:
				path = ExportProteusProxy.saveImage(exportDTO, "png");
				response.setUrl(path);
				response.setSuccess(true);
				break;
			case MO:
				String fileName = ExportProteusProxy.saveMOFile(exportDTO);
				response.setUrl(fileName);
				response.setSuccess(true);
				break;
			case PDF:
				System.out.println("export as pdf");
				path = ExportProteusProxy.savePDF(exportDTO);
				response.setUrl(path);
				response.setSuccess(true);
				break;

			}
		} catch (IOException e) {
			response.setSuccess(false);

		}
		return response;
	}

	@Override
	public String fetchOMCInfo() {
		return omcProxy.getLogMsg();
	}

	@Override
	public ComponentDTO getComponent(String wholeName) {
		// @sam this is the traditional java object way
		// try {
		// return DomainComponentProxy.getComponent(name);
		// } catch (ComponentNotFoundException e) {
		// System.err.println("Component not Found");
		// return null;
		// }
		return componentDTOMap.get(wholeName);
	}

	/*
	 * (non-Javadoc) // @ sam this is the new Json way, which will make the
	 * sending speed // faster
	 * 
	 * @see
	 * proteus.gwt.client.proxy.ProteusRemoteService#getComponentJSON(java.lang
	 * .String)
	 */
	@Override
	public String getComponentJSON(String name) {
		String componentFilesPath = ServerContext.get("ProteusRootPath")
				+ ServerContext.get("ProteusFilesPath") + "/ComponentJSON/"
				+ name;
		if (Utils.DEBUGUI)
			System.out.println("GetComponentJson: " + componentFilesPath);

		FileReader fr;
		BufferedReader br;
		String strLine = "";
		String strAll = "";
		try {
			fr = new FileReader(componentFilesPath);
			br = new BufferedReader(fr);
			while ((strLine = br.readLine()) != null) {
				strAll += strLine;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// Log.info("after read xml file"+ name);
		return strAll;
	}

	@Override
	public SimulationResultDTO mslSim(String resp) {
//		long timeStamp = MiscUtils.getTimeStampMilli();
		if (resp == null || resp.length() == 0)
			return null;
		if (omcProxy != null) {
			omcProxy.emptyMsgQ();
		}

		BufferedReader r = getBufferReader(resp);

		String model = ExtractInfo(r, HEADER_PREFIX_MODEL);
		String startT = ExtractInfo(r, HEADER_PREFIX_START_TIME);
		double startTime = Utils.parseDouble(startT);
		String stopT = ExtractInfo(r, HEADER_PREFIX_STOP_TIME);
		double stopTime = Utils.parseDouble(stopT);
		SimulationResultDTO simDTO = runMSLModel(model, startTime, stopTime);
//		Gson gson = createGson();
//
//		// convert java object to JSON format,
//		// and returned as JSON formatted string
//		String json = gson.toJson(simDTO);
		return simDTO; 
//		return timeStamp + "$" + json;
	}

	private Gson createGson() {
		return new GsonBuilder().setLongSerializationPolicy(
				LongSerializationPolicy.STRING).create();
	}

	private static Logger logger = Logger.getAnonymousLogger();

	@Override
	public String login(String userinfo) {
		BufferedReader r = getBufferReader(userinfo);
		String name = ExtractInfo(r, Constants.HEADER_USER);
		String pwd = ExtractInfo(r, Constants.HEADER_PASS);
		Users users = ProteuswebProxy.login(name, pwd);
		if (users != null) {
			Gson gson = createGson();
			String json = gson.toJson(users);
			return json;
		} else {
			return Constants.INVALID_USER;
		}
	}

	@Override
	public String checkLogin(String string) {
		BufferedReader r = getBufferReader(string);
		String name = ExtractInfo(r, Constants.HEADER_USER);
		String pwd = ExtractInfo(r, Constants.HEADER_PASS);
		Users users = ProteuswebProxy.checkLogin(name, pwd);
		if (users != null) {
			Gson gson = createGson();
			String json = gson.toJson(users);
			return json;
		} else {
			return Constants.INVALID_USER;
		}
	}

	@Override
	public String saveMOFile(String modelCode) {
		StringBuilder ErrorMsg = new StringBuilder();
		BufferedReader r = getBufferReader(modelCode);
		String mids = ExtractInfo(r, Constants.HEADER_MID);
		long mid = Long.valueOf(mids);
		String uids = ExtractInfo(r, Constants.HEADER_UID);

		String filePath = "";
		if (!ServerContext.isLocal()) {
			int uid = Integer.valueOf(uids);
			// do a few check up
			Content_type_mod mod = ProteuswebProxy.locateFileID(mid);
			if (mod == null) {
				ErrorMsg.append(Constants.FILE_NOT_FOUND_ERROR);
				return ErrorMsg.toString();
			}
			int fid = mod.getField_mo_fid();
			Files f = ProteuswebProxy.getFileLocation(uid, fid);
			if (f == null) {
				ErrorMsg.append(Constants.NOT_UR_MODEL_ERROR);
				return ErrorMsg.toString();
			}
			filePath = ServerContext.get("PwebRoot") + "/" + f.getFilepath();
		} else {
			// this is local computer version.
			// for debug and test purpose
			filePath = "C:\\tmp\\1.mo";
		}

		long id = Long.MIN_VALUE;
		try {
			File fout = ProteuswebProxy.writeMOfile(r, filePath);
			if (!fout.exists()) {
				ErrorMsg.append(Constants.FILE_WRITTEN_FAILED_ERROR);
				return ErrorMsg.toString();
			}
		} catch (IOException e) {
			ErrorMsg.append(Constants.FILE_IO_ERROR);
		}
		MOFileParser fileParser = new MOFileParser();
		try {
			id = fileParser.parser(mid, filePath, null, "");
		} catch (IOException e) {
			ErrorMsg.append("Parsing, Json Written error!");
			ErrorMsg.append(e.getMessage());
			e.printStackTrace();
		}

		if (id == mid) {
			ErrorMsg.append("Model Saved Successfully!");
		} else if (id < 0) {
			ErrorMsg.append("parser error");
		}
		return ErrorMsg.toString();
	}

	public static void main(String[] args) {

//		testSaveMoFile();
		
		testOMCProxy();
	}

	private static void testOMCProxy() {
		ProteusRemoteServiceImpl prsi = new ProteusRemoteServiceImpl();
		prsi.initOMCServer();
		OMCProxy omcProxy = new OMCProxy();
	    try {
			omcProxy.getStandardLibrary();
		} catch (ConnectException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void testSaveMoFile() {
		// TODO Auto-generated method stub
		ProteusRemoteServiceImpl prs = new ProteusRemoteServiceImpl();
		// prs.tempini();
		// prs
		// .runMSLModel("Modelica.Electrical.Analog.Examples.ChuaCircuit",
		// 0, 1);

		StringBuilder sb = new StringBuilder();
		String name = "username", pwd = "password";
		sb.append(Constants.HEADER_MID + 227 + "\n");
		sb.append(Constants.HEADER_UID + 7 + "\n");
		try {
			sb.append(read("c:/2.mo"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		long time0 = MiscUtils.getTimeStampMilli();
		prs.saveMOFile(sb.toString());
		// prs.getDemo("proteus.demoOne");
		long time1 = MiscUtils.getTimeStampMilli();
		System.err.println((time1 - time0) + "");
	}

	public static String read(String filePath) throws IOException {
		FileReader fr = new FileReader(filePath);
		BufferedReader br = new BufferedReader(fr);
		String strLine = "";
		StringBuffer allLine = new StringBuffer();
		while ((strLine = br.readLine()) != null) {
			allLine.append(strLine + "\n");
		}
		return allLine.toString();
	}

	@Override
	public String getModelJSON(Long modelId) {
		String componentFilesPath = ServerContext.get("ProteusRootPath")
				+ ServerContext.get("ProteusFilesPath") + "/ModelJSON/"
				+ modelId;
		if (Utils.DEBUG)
			System.out.println("get modelJson: " + componentFilesPath);

		FileReader fr;
		BufferedReader br;
		String strLine = "";
		String strAll = "";
		try {
			fr = new FileReader(componentFilesPath);
			br = new BufferedReader(fr);
			while ((strLine = br.readLine()) != null) {
				strAll += strLine;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// Log.info("after read xml file"+ name);
		return strAll;
	}

	@Override
	public String getDemoJSON(String modelId) {
		String componentFilesPath = ServerContext.get("ProteusRootPath")
				+ ServerContext.get("ProteusFilesPath") + "/ModelJSON/"
				+ modelId;
		if (Utils.DEBUG)
			System.out.println("get modelJson: " + componentFilesPath);

		FileReader fr;
		BufferedReader br;
		String strLine = "";
		String strAll = "";
		try {
			fr = new FileReader(componentFilesPath);
			br = new BufferedReader(fr);
			while ((strLine = br.readLine()) != null) {
				strAll += strLine;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// Log.info("after read xml file"+ name);
		return strAll;
	}

	@Override
	public String SimulateJSON(String model) {
		if (model == null || model.length() == 0)
			return null;
		if (omcProxy != null) {
			omcProxy.emptyMsgQ();
		}
		// try {
		// SimulationResultDTO simulate = sendJob(model);
		// // simulate.setTitle(timeStamp + "");
		// // if (simulate == null) {
		// // simulate = getFakeResult();
		// // }
		// String resultJson = "";
		// return resultJson;
		//
		// } catch (IOException e) {
	
	// e.printStackTrace();
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		return null;
	}


}
