-- 1. IT_SUPPORT TRAINING DATASETS (20 RECORDS)
INSERT INTO grievance_samples (description, department, is_test_sample) VALUES
('the classroom projector screen is blank and flashing a red hardware light status error', 'IT_SUPPORT', false),
('hostel wifi network connectivity keeps dropping out randomly causing log in portal disconnects', 'IT_SUPPORT', false),
('unable to access university student email profile due to a severe login auth protocol server crash', 'IT_SUPPORT', false),
('classroom desktop computer screen is completely frozen and unresponsive to keyboard mouse inputs', 'IT_SUPPORT', false),
('central digital library printer is showing offline network status due to a hardware paper jam error', 'IT_SUPPORT', false),
('forgot account credentials for the student registration database portal request quick password reset bean', 'IT_SUPPORT', false),
('laboratory computer workstations are missing critical network driver software installations making coding impossible', 'IT_SUPPORT', false),
('campus open access internet speed is unacceptably slow blocking submission of terminal lab reports', 'IT_SUPPORT', false),
('the automated security biometric scanning gate at main campus entrance is failing to read student id cards', 'IT_SUPPORT', false),
('attendance tracking software app keeps crashing on startup throwing generic application context runtime error exceptions', 'IT_SUPPORT', false),
('multimedia audio sound system speakers in the main auditorium are throwing loud hardware interference static noise', 'IT_SUPPORT', false),
('cannot connect personal laptop device to the campus secure cloud storage drive cluster access denied', 'IT_SUPPORT', false),
('the engineering lab software compiler license keys have expired throwing validation failure code blocks', 'IT_SUPPORT', false),
('smart class interactive smartboard system display touchscreen sensor is completely misaligned and broken', 'IT_SUPPORT', false),
('university mobile application portal profile page is failing to update academic records loading infinitely', 'IT_SUPPORT', false),
('hostel computer center local area network router port switch has failed cutting off internet throughput', 'IT_SUPPORT', false),
('digital portal payment transaction portal is completely down throwing database handshake connection timeouts', 'IT_SUPPORT', false),
('department desktop pc hard disk drive is making clicking noises failing to execute boot record sequences', 'IT_SUPPORT', false),
('e learning video streaming library server portal is unreachable from off campus proxy connections network error', 'IT_SUPPORT', false),
('the virtual learning management system database node is throwing concurrent transaction deadlock execution warnings', 'IT_SUPPORT', false);

-- 2. IT_SUPPORT HELD-OUT TEST DATASETS (10 RECORDS)
INSERT INTO grievance_samples (description, department, is_test_sample) VALUES
('wifi connection is not working in the study hall unable to download lecture slides file', 'IT_SUPPORT', true),
('classroom desktop computer mouse and keyboard hardware inputs are not responding at all', 'IT_SUPPORT', true),
('printer is not processing print commands showing an ink toner hardware warning code display', 'IT_SUPPORT', true),
('reset password link for student portal email activation is not sending registration token strings', 'IT_SUPPORT', true),
('the digital library terminal server is down throwing connection failure error blocks on login', 'IT_SUPPORT', true),
('laboratory internet connection throughput drops to zero blocking git push repository commits to cloud', 'IT_SUPPORT', true),
('the campus portal website is completely frozen throwing a bad gateway status routing failure error', 'IT_SUPPORT', true),
('classroom audio microphone amplifier hardware is broken causing loud static loop feedback across speakers', 'IT_SUPPORT', true),
('scanner machine in the administrative building network block is not capturing structural document uploads', 'IT_SUPPORT', true),
('virtual desktop environment infrastructure throws authentication handshake failure rejections for all student profiles', 'IT_SUPPORT', true);

-- 3. FACILITIES TRAINING DATASETS (20 RECORDS)
INSERT INTO grievance_samples (description, department, is_test_sample) VALUES
('the classroom air conditioner unit is leaking water heavily causing cooling fan motor failure', 'FACILITIES', false),
('broken wooden writing desk chairs in the main seminar hall are highly unsafe for students', 'FACILITIES', false),
('water pipeline leakage from ceiling in the ground floor science block washroom plumbing repair needed', 'FACILITIES', false),
('electrical socket wall switches in lab room 204 are burnt throwing sparks when active danger', 'FACILITIES', false),
('classroom window glass pane is cracked and shattered posing an immediate injury hazard to students', 'FACILITIES', false),
('main administrative block elevator lift is getting stuck between floors mechanical pulley cable failure warning', 'FACILITIES', false),
('canteen dining hall floor tiles are completely chipped and broken causing tripping hazards during rush hours', 'FACILITIES', false),
('auditorium emergency fire exit double doors are jammed shut blocked by broken old storage inventory furniture', 'FACILITIES', false),
('overhead light bulbs in block c study corridor are flickering badly causing eye strain during night shifts', 'FACILITIES', false),
('hostel room ceiling plaster mortar is peeling off and dropping debris down onto student bunk beds', 'FACILITIES', false),
('campus main parking lot storm drainage system grid is completely blocked with leaves causing flash flooding', 'FACILITIES', false),
('the drinking water cooling fountain filtration machine tank is rusted leaking foul smelling dirty liquid', 'FACILITIES', false),
('seminar hall wooden stage panel board structure is rotting and collapsing under weight structural damage', 'FACILITIES', false),
('block d washroom flush valves are broken out of order causing unsanitary conditions across rows', 'FACILITIES', false),
('classroom window frames are missing structural rubber weather seals letting rainwater soak the inner walls', 'FACILITIES', false),
('hostel block corridor fire extinguisher container brackets are broken dropping units to the floor layout', 'FACILITIES', false),
('campus botanical garden walkway concrete blocks are loose out of alignment making walking dangerous', 'FACILITIES', false),
('administrative building outer roofing gutters are choked pouring mud sheets straight onto the pathway doors', 'FACILITIES', false),
('the main campus perimeter brick wall structure has developed deep vertical load cracks stability check needed', 'FACILITIES', false),
('library basement room exhaust air ventilation shafts are choked causing dark damp mold buildup', 'FACILITIES', false);

-- 4. FACILITIES HELD-OUT TEST DATASETS (10 RECORDS)
INSERT INTO grievance_samples (description, department, is_test_sample) VALUES
('the ac unit in room 102 is hot not cooling at all fan motor failure warning', 'FACILITIES', true),
('broken writing pad desk chairs in the lecture theatre need urgent structural wood repair work', 'FACILITIES', true),
('washroom flush pipeline valve is broken causing non stop water leakage flooding the floor layouts', 'FACILITIES', true),
('electrical wall socket plug point is completely loose out of the wall panel wiring exposed danger', 'FACILITIES', true),
('classroom main door entrance lock handle is jammed and broken trapping students inside the room', 'FACILITIES', true),
('corridor ceiling light tube bulb is flickering out of order making the pathway completely dark', 'FACILITIES', true),
('staircase metal safety handrail railing is loose detached from concrete wall anchoring parameters unsafe', 'FACILITIES', true),
('library reading hall floor carpet is torn up causing serious tripping hazards for passing student lines', 'FACILITIES', true),
('hostel bathroom drain trap is completely clogged with debris causing dirty backup waste accumulation', 'FACILITIES', true),
('main building window glass window frame is completely shattered throwing sharp glass shards onto paths', 'FACILITIES', true);

-- 5. ADMINISTRATION TRAINING DATASETS (20 RECORDS)
INSERT INTO grievance_samples (description, department, is_test_sample) VALUES
('tuition fee online transaction payment processed successfully but digital portal failed to output receipt document', 'ADMINISTRATION', false),
('severe processing delay in student smart id card generation and registration data verification check lists', 'ADMINISTRATION', false),
('clearance form sign off signature processing is delayed due to absolute administrative staff scheduling confusion', 'ADMINISTRATION', false),
('official academic transcript request profile modification forms are stuck in pending status queue for weeks', 'ADMINISTRATION', false),
('course registration database portal threw pre requisite waiver error rejections despite manual department approval', 'ADMINISTRATION', false),
('duplicate tuition fee deduction transaction anomaly hit account billing statement balance correction profile needed', 'ADMINISTRATION', false),
('scholarship allocation application processing documentation metrics are stuck in internal audit review cycles', 'ADMINISTRATION', false),
('exam schedule time matrix configuration has direct timing conflicts between two mandatory core engineering branches', 'ADMINISTRATION', false),
('academic calendar dates sheet modifications were published late causing confusion regarding holiday slot declarations', 'ADMINISTRATION', false),
('bus pass concession token issuance office window closes early blocking travel clearance form parameter updates', 'ADMINISTRATION', false),
('personal profile field metrics name data parameters spelling errors matching records modified validation check', 'ADMINISTRATION', false),
('provisional degree certificate verification workflow pipeline is stuck at registrar approval processing phase', 'ADMINISTRATION', false),
('no response from the evaluation controller room regarding revaluation fee deposit validation request metrics', 'ADMINISTRATION', false),
('hostel room allotment merit list generation logic features algorithm bias prioritizing wrong enrollment index slots', 'ADMINISTRATION', false),
('migration certificate collection documentation rules are highly redundant requiring five separate copies signed', 'ADMINISTRATION', false),
('sports quota mark weight allocation parameter calculations are missing from the semester merit ranking sheets', 'ADMINISTRATION', false),
('graduation verification certificate processing fees processed but database account balance ledger not updated', 'ADMINISTRATION', false),
('course drop request form drop deadline parameter modifications rejected due to an system time zone error link', 'ADMINISTRATION', false),
('academic board registration enrollment number generation lists feature duplicate entries mismatch crash tracking', 'ADMINISTRATION', false),
('examination hall ticket generation tracking link returns blank dashboard pages for incoming backlog profiles', 'ADMINISTRATION', false);

-- 6. ADMINISTRATION HELD-OUT TEST DATASETS (10 RECORDS)
INSERT INTO grievance_samples (description, department, is_test_sample) VALUES
('tuition fee double payment transaction processing ledger error requires an immediate billing statement balance refund', 'ADMINISTRATION', true),
('smart id card printing generation workflow tracking queue has been stuck in pending processing for months', 'ADMINISTRATION', true),
('clearance form administrative signature loops require too many manual desks processing documentation check files', 'ADMINISTRATION', true),
('official academic transcript graduation verification document copy tracking order is delayed status not changing', 'ADMINISTRATION', true),
('course registration portal blocks enrollment into optional elective tracks throwing system prerequisite error matrix logs', 'ADMINISTRATION', true),
('semester examination date timetable configuration sheet has a direct scheduling collision with laboratory test hours', 'ADMINISTRATION', true),
('merit scholarship funding installment processing timelines are running late balance ledger entries showing zero values', 'ADMINISTRATION', true),
('student personal data validation sheet features incorrect date of birth parameters record update request parameters', 'ADMINISTRATION', true),
('provisional degree verification document tracking portal link is completely unresponsive registrar review queue locked', 'ADMINISTRATION', true),
('revaluation form submission fee transaction ledger records are not showing up inside the account billing summary dashboard', 'ADMINISTRATION', true);