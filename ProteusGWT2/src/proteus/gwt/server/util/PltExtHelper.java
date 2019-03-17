package proteus.gwt.server.util;

import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import proteus.gwt.shared.datatransferobjects.PlotDataDTO;
import proteus.gwt.shared.datatransferobjects.Range;
import proteus.gwt.shared.datatransferobjects.Values2D;
import proteus.gwt.shared.types.IllegalDimensionsException;

import com.google.gwt.user.client.rpc.IsSerializable;


public class PltExtHelper implements IsSerializable {

	private static final String TITLE_TEXT = "TitleText";

	private static final String X_LABEL = "XLabel";

	private static final String DATA_SET = "DataSet";


	public static PlotDataDTO[] readPltPlot(File pltFile,
			String[] plotVars, List<PlotDataDTO> list) throws IOException, ParseException {
		
		BufferedReader r = new BufferedReader(new FileReader(pltFile));
		String line;
		String titleText = "", xLabel = "";
		
		while ((line = r.readLine()) != null) {
			if (line.startsWith(TITLE_TEXT)) {
				int index = line.indexOf(":");
				if (index >= 0) {
					titleText = line.substring(index + 1).trim();
				}
			} else if (line.startsWith(X_LABEL)) {
				int index = line.indexOf(":");
				if (index >= 0) {
					xLabel = line.substring(index + 1).trim();
				}
			} else if (line.startsWith(DATA_SET)) {
				String dataSet = "$UNKNOWN$";
				int index = line.indexOf(":");
				if (index >= 0) {
//					why to lowerCase
					//16 Feb, 2011
					dataSet = line.substring(index + 1).trim();//.toLowerCase();
				}
				boolean include = false;
				if (plotVars != null) {
					OUTER: for (String s : plotVars) {
						String[] tokens = s.split(",[\\s]*");
						for (String t : tokens) {
							if (t.equalsIgnoreCase(dataSet)) {
								include = true;
								break OUTER;
							}
						}
					}
				} else {
					include = true;
				}
				if (include) {
					PlotDataDTO pd = new PlotDataDTO(dataSet,
							titleText, xLabel, dataSet);
					double minx = Double.MAX_VALUE, maxx = -Double.MAX_VALUE;
					double miny = Double.MAX_VALUE, maxy = -Double.MAX_VALUE;
					List<Point2D.Double> points = new ArrayList<Point2D.Double>();
					while ((line = r.readLine()) != null && line.length() > 0) {
						String[] tokens = line.split(",[\\s]*");
						if (tokens.length != 2) {
							throw new ParseException("<x>, <y>", line);
						}
						// Oct 15 @sam ignore boolean type result for now
						if (!isaNumber(tokens[0]) || !isaNumber(tokens[1])) {
							System.out.println(dataSet + " is not a Number! "
									+ tokens[0] + " " + tokens[1]);
							break;
						}
						try {
							double x = Double.parseDouble(tokens[0]);
							double y = Double.parseDouble(tokens[1]);
							points.add(new Point2D.Double(x, y));
							minx = Math.min(minx, x);
							maxx = Math.max(maxx, x);
							miny = Math.min(miny, y);
							maxy = Math.max(maxy, y);
						} catch (NumberFormatException e) {
							throw new ParseException("<x>, <y>", line);
						}
					}
					int len = points.size();
					double[] xValues = new double[len];
					double[] yValues = new double[len];
					for (int i = 0; i < len; i++) {
						Point2D.Double p = points.get(i);
						xValues[i] = p.x;
						yValues[i] = p.y;
					}
					try {
						pd.setValues(new Values2D(xValues, yValues));
					} catch (IllegalDimensionsException e) {
						e.printStackTrace();
					}
						Range xr = new Range(minx, maxx);
						pd.setXRange(xr);
						Range yr = new Range(miny, maxy);
						pd.setYRange(yr);
						list.add(pd);
				}
			}
		}
		return list.toArray(new PlotDataDTO[0]);
	}

	private static boolean isaNumber(String s) {
		return (s.charAt(0) < 58 && s.charAt(0) > 42) ? true : false;
	}

	public static void writePltPlot(File pltFile, PlotDataDTO[] data)
			throws IOException {
		PrintWriter w = new PrintWriter(new FileWriter(pltFile));
		for (PlotDataDTO pd : data) {
			w.println(TITLE_TEXT + ": " + pd.getTitle());
			w.println(X_LABEL + ": " + pd.getXLabel());
			w.println(DATA_SET + ": " + pd.getName());
			Values2D values = pd.getValues();
			for (int i = 0; i < values.xValues.length; i++) {
				w.println(values.xValues[i] + "," + values.yValues[i]);
			}
			w.println("");
		}
	}

	public static void main(String[] args) {
		// File f = new
		// File("C:\\Documents and Settings\\sam\\.Proteus10\\jamshell\\pfGXBlS2oH\\Modelica.Mechanics.MultiBody.Examples.Systems.RobotR3.fullRobot_res.plt");
		
//		System.out.println(((MLDouble)matfilereader.getMLArray( "atest" )).contentToString());
		
	}

	public static PlotDataDTO[] readPltPlot(File pltFile, List<PlotDataDTO> list)
		throws IOException, ParseException {
			return readPltPlot(pltFile, null, list);
	}
}
