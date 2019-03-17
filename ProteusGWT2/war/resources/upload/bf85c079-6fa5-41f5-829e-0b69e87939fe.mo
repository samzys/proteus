model Circuit "description string"
Modelica.Electrical.Analog.Basic.Ground ground () annotation(extent = [-19, -34; -4, -16]);
Modelica.Electrical.Analog.Basic.Resistor resistor () annotation(extent = [-28, -64; -6, -49]);
Modelica.Electrical.Analog.Basic.Capacitor capacitor () annotation(extent = [9, -64; 31, -49]);
Modelica.Electrical.Analog.Sources.SineVoltage sineVoltage () annotation(extent = [-61, -64; -39, -49]);
annotation(Icon() ,
Diagram() ,
RunConfigurations() ,
LibFiles = (""));
equation
connect (resistor.n, capacitor.p);
connect (sineVoltage.n, resistor.p);
connect (sineVoltage.p, ground.p);
connect (capacitor.n, ground.p);
end Circuit;

