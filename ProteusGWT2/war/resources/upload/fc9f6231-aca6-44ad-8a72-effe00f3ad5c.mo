model MyModel "description string"
Modelica.Blocks.Math.InverseBlockConstraints inverseblockconstraints0 () annotation(Placement(transformation(origin = {136, 113},
extent = {{-66, -39}, {66, 39}},
rotation = 0) ) );
Modelica.Blocks.Math.Gain gain0 (k = 1) annotation(Placement(transformation(origin = {362, 111},
extent = {{-33, -33}, {33, 33}},
rotation = 0) ) );
annotation(Icon() ,
Diagram() ,
LinkMap() ,
RunConfigurations() ,
LibFiles = (""));
equation
connect (inverseblockconstraints0.y1, gain0.u)annotation(Line(points = {{313.0, 152.0}, {313.0, 143.0}}) );
end MyModel;
