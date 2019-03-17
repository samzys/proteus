model MyModel "description string"
Modelica.Mechanics.Rotational.Components.Inertia inertia0 (J=5, w(fixed = true, start = 0),
        phi(fixed = true,
          start=1.570796326794897)) annotation(Placement(transformation(origin = {3, 254},
extent = {{-30, -30}, {30, 30}},
rotation = 0) ) );
Modelica.Mechanics.Rotational.Components.Fixed fixed0 (phi0(start = 0.0) = 0) annotation(Placement(transformation(origin = {-397, 209},
extent = {{-30, -30}, {30, 30}},
rotation = 0) ) );
Modelica.Mechanics.Rotational.Components.SpringDamper springdamper0 (c=20e3 ,
d=50) annotation(Placement(transformation(origin = {-234, 269},
extent = {{-30, -30}, {30, 30}},
rotation = 0) ) );
annotation(Icon() ,
Diagram() ,
LinkMap() ,
RunConfigurations() ,
LibFiles = (""));
equation
connect (springdamper0.flange_b, inertia0.flange_a)annotation(Line(points = {{-116, 269}, {-116, 254}}) );
connect (springdamper0.flange_a, fixed0.flange)annotation(Line(points = {{-331, 269}, {-331, 209}}) );
end MyModel;
