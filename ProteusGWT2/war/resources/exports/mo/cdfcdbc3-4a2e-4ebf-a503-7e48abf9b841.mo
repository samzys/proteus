model MyModel "description string"
Modelica.Blocks.Continuous.Integrator integrator0 (initType = Modelica.Blocks.Types.Init.InitialState,
k = 1,
y_start = 0) annotation(Placement(transformation(origin = {-111, 257},
extent = {{33, 33}, {-33, -33}},
rotation = 0) ) );
Modelica.Blocks.Continuous.LimIntegrator limintegrator0 (k = 1,
outMin = -outMax,
outMax = 1,
limitsAtInit = true,
initType = Modelica.Blocks.Types.Init.InitialState,
y_start = 0) annotation(Placement(transformation(origin = {140, 339},
extent = {{-33, -33}, {33, 33}},
rotation = 0) ) );
annotation(Icon() ,
Diagram() ,
LinkMap() ,
RunConfigurations() ,
LibFiles = (""));
equation
connect (integrator0.y, limintegrator0.u)annotation(Line(points = {{-56, 221}, {-56, 339}}) );
end MyModel;
