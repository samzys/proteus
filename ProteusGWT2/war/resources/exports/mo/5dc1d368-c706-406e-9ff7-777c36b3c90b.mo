model MyModel "description string"
Modelica.Blocks.Continuous.Integrator integrator0 (initType = Modelica.Blocks.Types.Init.InitialState,
k = 1,
y_start = 0) annotation(Placement(transformation(origin = {-359, 275},
extent = {{33, 33}, {-33, -33}},
rotation = 0) ) );
Modelica.Blocks.Continuous.Integrator integrator1 (initType = Modelica.Blocks.Types.Init.InitialState,
k = 1,
y_start = 0) annotation(Placement(transformation(origin = {-193, 283},
extent = {{-33, -33}, {33, 33}},
rotation = 0) ) );
annotation(Icon() ,
Diagram() ,
LinkMap() ,
RunConfigurations() ,
LibFiles = (""));
equation
connect (integrator0.u, integrator1.u)annotation(Line(points = {{-296, 314.6}, {-296, 283}}) );
end MyModel;
