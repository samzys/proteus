model MyModel "description string"
Modelica.Mechanics.Rotational.Components.Fixed fixed0 (phi0(start = 0.0) = 0) annotation(Placement(transformation(origin = {-369, 270},
extent = {{-30, -30}, {30, 30}},
rotation = 0) ) );
Modelica.Mechanics.Rotational.Components.Inertia inertia0 (J(start = 1.0) = 1,
stateSelect = StateSelect.default,
phi(start = 0.0) ,
w(start = 0.0) ,
a(start = 0.0) ) annotation(Placement(transformation(origin = {-136, 332},
extent = {{-30, -30}, {30, 30}},
rotation = 0) ) );
Modelica.Mechanics.Rotational.Components.SpringDamper springdamper0 (tau(start = 0.0) ,
stateSelect = StateSelect.prefer,
phi_nominal(start = 0.0) = 1e-4,
a_rel(start = 0.0) ,
phi_rel(start = 0.0) ,
w_rel(start = 0.0) ,
d(start = 0.0) = 0,
c(start = 100000.0) = 1.0e5,
phi_rel0(start = 0.0) = 0) annotation(Placement(transformation(origin = {-275, 344},
extent = {{-30, -30}, {30, 30}},
rotation = 0) ) );
annotation(Icon() ,
Diagram() ,
LinkMap() ,
RunConfigurations() ,
LibFiles = (""));
equation
connect (springdamper0.flange_b, inertia0.flange_a)annotation(Line(points = {{-206, 344}, {-206, 332}}) );
connect (springdamper0.flange_a, fixed0.flange)annotation(Line(points = {{-337, 344}, {-337, 270}}) );
end MyModel;
