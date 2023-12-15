mvn package

scp ./target/projet-clashroyal-0.0.1.jar lsd2:projet-clashroyal-0.0.1.jar

hdfs dfs -rm -R /user/{dossier_personnel}/Projet

yarn jar projet-clashroyal-0.0.1.jar /user/auber/data_ple/clashroyale/gdc_battles.nljson Projet

hdfs dfs -ls /user/mbahloul001/Projet

hdfs dfs -cat /user/mbahloul001/Projet/part-r-00000



