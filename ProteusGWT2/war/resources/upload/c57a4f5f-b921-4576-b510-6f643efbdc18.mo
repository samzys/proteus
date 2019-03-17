model Model "description string"
Modelica.Electrical.Analog.Basic.Ground Ground () annotation(extent = [9, 68; 47, 114]);
Modelica.Electrical.Analog.Basic.Resistor Resistor () annotation(extent = [223, 137; 283, 197]);
Modelica.Electrical.Analog.Basic.Capacitor Capacitor () annotation(extent = [23, 139; 61, 185]);
Modelica.Electrical.Analog.Sources.ConstantVoltage ConstantVoltage () annotation(extent = [132, 145; 170, 191]);
annotation(Icon() ,
Diagram() ,
LinkMap() ,
RunConfigurations() ,
LibFiles = (""));
equation
connect (Resistor.n, Capacitor.p);
connect (Capacitor.n, ConstantVoltage.p);
connect (ConstantVoltage.n, Ground.p);
connect (Resistor.p, Ground.p);
end Model;

