% Matlab source code for thrmodynamic simulation
% The resulting simulation reproduces the thermodynamic results of D=500 nm PMMA beads on silicon in air
%% Parameters input:
% Properties of PMMA used as example
pi = 3.14;          %
Cs = 1387;          % Particle specific heat capacity [J/(kg*k)]
rho = 1190;         % Particle density [kg/m³] 
D = 500e-9;         % Particle diameter [m]
V = 4/3*pi*(D/2)^3; % Particle volume [m³]
m = V*rho;          % Particle mass [kg]
C = m*Cs;           % Particle heat capacity [J/K]

hS = 2.6e-7;        % Particle-enviroment heat transfer parameter [W/K]

sigma_abs = 4.2041e-14; % Particle absoprtion cross section [m²]

tau_ir = 980e-9;    % IR laser pulse duration [s]
f_ir = 150e+3;      % IR laser repetition frequency [Hz]
p_ir = 10e-3;       % IR laser average power [W]
s_ir = 1.2566e-9;   % Estimated IR focal spot area [m²]
I_ir = (p_ir/tau_ir/f_ir)/s_ir; % Calculated IR intensity [W/m²]
%I_ir = 5.4134e+7; % IR intensity [W/m²]

Q_in = I_ir*sigma_abs; % Particle heat source [W]

T0 = 273.15;        % Ambient temperature
%% Construct temperature modulation fucntion 
% t <= tau_ir, Heating process
decay_constant = C/hS;
Tt_heating = @(t) (Q_in/hS)*(1-(exp(-(1/decay_constant)*t)))+T0;
T_max = Tt_heating(tau_ir);
delta_T_max = T_max - T0;

% t > tau_ir, Decay process
Tt_decay = @(t) (T_max-T0) *exp(-(1/decay_constant)*t)+T0;

%% Plot temperature modulation of 500 nm PMMA under single IR pulse 
t_test = linspace(0,10e-6, 1000);
Tt = @(t) Tt_heating(t).*(t<= tau_ir) + Tt_decay(t-tau_ir).*(t> tau_ir);

figure;
plot(t_test, Tt(t_test),'r','LineWidth',2)
xlim([-1e-6 11e-6])
xlabel('Time (s)')
ylabel('Temperature (K)')
legend('D=500 nm PMMA bead')