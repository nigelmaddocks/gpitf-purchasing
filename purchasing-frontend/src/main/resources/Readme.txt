This project uses lombok. Ensure that the build process also uses it. You may have to add parameters to the jenkins build pipeline such as
-vmargs -javaagent:lombok.jar -Xbootclasspath/a:lombok.jar

IDE:
Close IDE. 
Run: sudo java -jar lombok-1.18.2.jar
Open IDE, Maven update the project and re-launch project.

Database:
Sample organisational data is setup with the migration script db/migration/V003__dynamic_data.sql

Full organisational data can be setup by running the controller endpoints:
(the following use the ODA ORD REST interface)
- /dataload/CCGs
- /dataload/GPs  (takes about 10 mins)
- /dataload/CSUs
- /dataload/SSs  (for shared services)
(the following use user-selected csv upload files)
- /dataload/Day0CSUs (for CSU/SS --> CCG data, see below)
- /dataload/PatientNumbers (for updating Patient numbers for GP Practices)

CSU --> CCG data is obtained by parsing a CSV file. The process for getting this file is as as follows:

	Login into Live TdB.
	Go to Tree Interface
	Choose “IT Service Provider” hierarchy
	Select “NHS England”
	Choose Reports --> “Organisation Downloads”
	On the new screen, select:
	            Organisations: Clinical Commissioning Groups
	            Type: Organisation Details
	Press “Run Download”
	 
	This outputs a list of CCGs and their parent CSUs in a csv file


