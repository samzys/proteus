 model HeatingResistor "Temperature dependent electrical resistor"
    parameter Modelica.SIunits.Resistance R_ref(start=1)
      "Resistance at temperature T_ref";
    parameter Modelica.SIunits.Temperature T_ref=300.15 "Reference temperature";
    parameter Modelica.SIunits.LinearTemperatureCoefficient alpha=0
      "Temperature coefficient of resistance (R = R_ref*(1 + alpha*(heatPort.T - T_ref))";
    extends Modelica.Electrical.Analog.Interfaces.OnePort;
    extends Modelica.Electrical.Analog.Interfaces.ConditionalHeatPort(T = T_ref, useHeatPort=true);
    Modelica.SIunits.Resistance R
      "Resistance = R_ref*(1 + alpha*(intenalHeatPort.T - T_ref))";
    annotation (__Dymola_structurallyIncomplete=true,
      Diagram(coordinateSystem(preserveAspectRatio=true, extent={{-100,-100},{
              100,100}}), graphics={
          Line(points={{-110,20},{-85,20}}, color={160,160,164}),
          Polygon(
            points={{-95,23},{-85,20},{-95,17},{-95,23}},
            lineColor={160,160,164},
            fillColor={160,160,164},
            fillPattern=FillPattern.Solid),
          Line(points={{90,20},{115,20}}, color={160,160,164}),
          Line(points={{-125,0},{-115,0}}, color={160,160,164}),
          Line(points={{-120,-5},{-120,5}}, color={160,160,164}),
          Text(
            extent={{-110,25},{-90,45}},
            lineColor={160,160,164},
            textString="i"),
          Polygon(
            points={{105,23},{115,20},{105,17},{105,23}},
            lineColor={160,160,164},
            fillColor={160,160,164},
            fillPattern=FillPattern.Solid),
          Line(points={{115,0},{125,0}}, color={160,160,164}),
          Text(
            extent={{90,45},{110,25}},
            lineColor={160,160,164},
            textString="i")}),
      Icon(coordinateSystem(preserveAspectRatio=true, extent={{-100,-100},{100,
              100}}), graphics={
          Line(points={{-90,0},{-70,0}}, color={0,0,255}),
          Line(points={{70,0},{90,0}}, color={0,0,255}),
          Rectangle(
            extent={{-70,30},{70,-30}},
            lineColor={0,0,255},
            fillColor={255,255,255},
            fillPattern=FillPattern.Solid),
          Line(points={{-52,-50},{48,50}}, color={0,0,255}),
          Polygon(
            points={{40,52},{50,42},{54,56},{40,52}},
            lineColor={0,0,255},
            fillColor={0,0,255},
            fillPattern=FillPattern.Solid),
          Line(
            visible=useHeatPort,
            points={{0,-100},{0,-30}},
            color={127,0,0},
            smooth=Smooth.None,
            pattern=LinePattern.Dot),
          Text(
            extent={{-156,109},{144,69}},
            textString="%name",
            lineColor={0,0,255})}),
      Documentation(info="<HTML>
<p>This is a model for an electrical resistor where the generated heat
is dissipated to the environment via connector <b>heatPort</b> and where
the resistance R is temperature dependent according to the following
equation:</p>
<pre>    R = R_ref*(1 + alpha*(heatPort.T - T_ref))
</pre>
<p><b>alpha</b> is the <b>temperature coefficient of resistance</b>, which
is often abbreviated as <b>TCR</b>. In resistor catalogues, it is usually
defined as <b>X [ppm/K]</b> (parts per million, similarly to per centage)
meaning <b>X*1.e-6 [1/K]</b>. Resistors are available for 1 .. 7000 ppm/K,
i.e., alpha = 1e-6 .. 7e-3 1/K;</p>

<p>
Via parameter <b>useHeatPort</b> the heatPort connector can be enabled and disabled
(default = enabled). If it is disabled, the generated heat is transported implicitly
to an internal temperature source with a fixed temperature of T_ref.<br>
If the heatPort connector is enabled, it must be connected.
</p>

</HTML>
",   revisions="<html>
<ul>
<li><i> August 07, 2009   </i>
       by Anton Haumer<br> temperature dependency of resistance added<br>
       </li>
<li><i> March 11, 2009   </i>
       by Christoph Clauss<br> conditional heat port added<br>
       </li>
<li><i> 2002   </i>
       by Anton Haumer<br> initially implemented<br>
       </li>
</ul>
</html>"));
  equation
    assert((1 + alpha*(T_heatPort - T_ref)) >= Modelica.Constants.eps, "Temperature outside scope of model!");
    R = R_ref*(1 + alpha*(T_heatPort - T_ref));
    v = R*i;
    LossPower = v*i;
  end HeatingResistor;
