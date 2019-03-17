within Default;

model Sensor
  Real x_out;
  Real y_out;
  Real x(start=0.1);
  Real y(start=0.1);
      annotation (
      Diagram(coordinateSystem(
          preserveAspectRatio=true,
          extent={{-100,-100},{100,100}},
          grid={0.5,0.5}), graphics),
      Icon(coordinateSystem(
          preserveAspectRatio=true,
          extent={{-100,-100},{100,100}},
          grid={0.5,0.5}), graphics={
          Rectangle(
            extent={{-99.5,100},{100,-100}},
            lineColor={0,0,0},
            fillColor={192,192,192},
            fillPattern=FillPattern.Solid),
          Bitmap(extent={{-75.5,98.25},{87,-96.75}}, fileName=
                "./USBIcon.bmp"),
          Text(
            extent={{-111.5,130},{108.5,100}},
            textString="%name",
            lineColor={0,0,255}),
          Text(
            extent={{-104.5,-104},{115,-128}},
            lineColor={0,0,0},
            textString="mLoad=%mLoad")}),
      experiment(StopTime=2));
equation
  der(x) = y;
  der(y) = x;
  x_out = x;
  y_out = y;
end Sensor;

