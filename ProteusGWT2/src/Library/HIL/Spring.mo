  model BodyShape1
    "Rigid body with mass, inertia tensor, different shapes for animation, and two frame connectors (12 potential states)"

    import SI = Modelica.SIunits;
    import C = Modelica.Constants;
    import Modelica.Mechanics.MultiBody.Types;

    Modelica.Mechanics.MultiBody.Interfaces.Frame_a frame_a
      "Coordinate system fixed to the component with one cut-force and cut-torque"
                               annotation (Placement(transformation(extent={{
              -116,-16},{-84,16}}, rotation=0)));
    Modelica.Mechanics.MultiBody.Interfaces.Frame_b frame_b
      "Coordinate system fixed to the component with one cut-force and cut-torque"
                               annotation (Placement(transformation(extent={{84,
              -16},{116,16}}, rotation=0)));
   Modelica.Mechanics.MultiBody.Parts.FixedTranslation frameTranslation(r=r, animation=false)
      annotation (Placement(transformation(extent={{-20,-20},{20,20}}, rotation=
             0)));
     Modelica.Mechanics.MultiBody.Parts.Body body(
      r_CM=r_CM,
      m=m,
      I_11=I_11,
      I_22=I_22,
      I_33=I_33,
      I_21=I_21,
      I_31=I_31,
      I_32=I_32,
      animation=false,
      sequence_start=sequence_start,
      angles_fixed=angles_fixed,
      angles_start=angles_start,
      w_0_fixed=w_0_fixed,
      w_0_start=w_0_start,
      z_0_fixed=z_0_fixed,
      z_0_start=z_0_start,
      useQuaternions=useQuaternions,
      enforceStates=enforceStates,
      sequence_angleStates=sequence_angleStates)
      annotation (Placement(transformation(extent={{-27.3333,-70.3333},{13,-30}},
            rotation=0)));
              end BodyShape1;
