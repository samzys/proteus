within Modelica.Mechanics.Translational.Examples;
model InitialConditions "Setting of initial conditions"
	extends Icons.Example;
	Components.Fixed fixed2(s0=1)annotation(Placement(transformation(extent={{-100,60},{-80,80}})));
	Components.Spring s2(
		c=1e3,
		s_rel0=2)annotation(Placement(transformation(extent={{-60,60},{-40,80}})));
	Components.Mass m3(
		m=1,
		s(
			start=4.5,
			fixed=true),
		L=3,
		v(fixed=true))annotation(Placement(transformation(extent={{-20,60},{0,80}})));
	Components.SpringDamper sd2(
		c=111,
		d=1,
		s_rel0=4)annotation(Placement(transformation(extent={{20,60},{40,80}})));
	Components.Mass m4(
		m=1,
		s(
			start=12.5,
			fixed=true),
		L=5,
		v(fixed=true))annotation(Placement(transformation(extent={{60,60},{80,80}})));
	Components.Fixed fixed1(s0=-1)annotation(Placement(transformation(extent={{-100,-20},{-80,0}})));
	Components.Spring s1(
		s_rel(
			start=1,
			fixed=true),
		c=1e3,
		s_rel0=1)annotation(Placement(transformation(extent={{-58,-20},{-38,0}})));
	Components.Mass m1(
		m=1,
		L=1,
		v(fixed=true))annotation(Placement(transformation(extent={{-20,-20},{0,0}})));
	Components.SpringDamper sd1(
		s_rel(
			start=1,
			fixed=true),
		v_rel(fixed=true),
		c=111,
		d=1,
		s_rel0=1)annotation(Placement(transformation(extent={{20,-20},{40,0}})));
	Components.Mass m2(
		m=1,
		L=2)annotation(Placement(transformation(extent={{60,-20},{80,0}})));
	equation
		connect(s2.flange_a, fixed2.flange) annotation (Line(
		    points={{-60,70},{-90,70}},
		    color={0,127,0},
		    smooth=Smooth.None));
		connect(s1.flange_a, fixed1.flange) annotation (Line(
		    points={{-58,-10},{-90,-10}},
		    color={0,127,0},
		    smooth=Smooth.None));
		connect(m1.flange_a, s1.flange_b) annotation (Line(
		    points={{-20,-10},{-38,-10}},
		    color={0,127,0},
		    smooth=Smooth.None));
		connect(sd1.flange_a, m1.flange_b) annotation (Line(
		    points={{20,-10},{0,-10}},
		    color={0,127,0},
		    smooth=Smooth.None));
		connect(m2.flange_a, sd1.flange_b) annotation (Line(
		    points={{60,-10},{40,-10}},
		    color={0,127,0},
		    smooth=Smooth.None));
		connect(m4.flange_a, sd2.flange_b) annotation (Line(
		    points={{60,70},{40,70}},
		    color={0,127,0},
		    smooth=Smooth.None));
		connect(sd2.flange_a, m3.flange_b) annotation (Line(
		    points={{20,70},{0,70}},
		    color={0,127,0},
		    smooth=Smooth.None));
		connect(m3.flange_a, s2.flange_b) annotation (Line(
		    points={{-20,70},{-40,70}},
		    color={0,127,0},
		    smooth=Smooth.None));
	public
		annotation(
			viewinfo[1](
				recentpage=10,
				size(
					cx=61,
					cy=31),
				typename="ClassSymbolInfo"),
			viewinfo[2](
				rotation=0,
				labelposition=0,
				position(
					left=0,
					top=0,
					right=60,
					bottom=60),
				typename="ObjectInfo"),
			viewinfo[3](
				tabGroupAlignment=589834,
				viewSettings(
					scrollPos(
						x=0,
						y=0),
					Font(
						height=80,
						weight=0,
						charset=1,
						clipprecision=0,
						quality=0,
						pitchandfamily=0,
						facename="ËÎÌו"),
					smallFont(
						weight=0,
						charset=1,
						clipprecision=0,
						quality=0,
						pitchandfamily=0,
						facename="ËÎÌו"),
					clrRaster=8421504,
					formatLabel="&name",
					szView(
						cx=20000,
						cy=20000)),
				typename="ModelInfo"),
			Diagram(coordinateSystem(extent={{-100,-100},{100,100}})),
LinkMap(),
			Documentation(info="<html>\n<p>\nThere are several ways to set initial conditions.\nIn the first system the position of the mass m3 was defined\nby using the modifier s(start=4.5), the position of m4 by s(start=12.5).\nThese positions were chosen such that the system is a rest. To calculate\nthese values start at the left (Fixed1) with a value of 1 m. The spring\nhas an unstreched length of 2 m and m3 an length of 3 m, which leads to\n</p>\n\n<pre>\n        1   m (fixed1)\n      + 2   m (spring s2)\n      + 3/2 m (half of the length of mass m3)\n      -------\n        4,5 m = s(start = 4.5) for m3\n      + 3/2 m (half of the length of mass m3)\n      + 4   m (springDamper 2)\n      + 5/2 m (half of length of mass m4)\n      -------\n       12,5 m = s(start = 12.5) for m4\n</pre>\n\n<p>\nThis selection of initial conditions has the effect that Dymola selects\nthose variables (m3.s and m4.s) as state variables.\nIn the second example the length of the springs are given as start values\nbut they cannot be used as state for pure springs (only for the spring/damper\ncombination). In this case the system is not at rest.\n</p>\n\n<p>\n<IMG SRC=../Images/Translational/Fig.translational.examples.InitialConditions.png>\n</p>\n\n\n</html>\n"),
			experiment(
				StopTime=5,
				StartTime=0,
				Interval=2.33640564385659e-307,
				NumberOfIntervals=6357112));
end InitialConditions;
