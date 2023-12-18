 _____ _       _           _   _____                         _               _____             _   
|  __ \ |     | |         | | /  ___|                       (_)             |_   _|           | |  
| |  \/ | ___ | |__   __ _| | \ `--. _ __   __ _ _ __  _ __  _ _ __   __ _    | |_ __ ___  ___| |_
| | __| |/ _ \| '_ \ / _` | |  `--. \ '_ \ / _` | '_ \| '_ \| | '_ \ / _` |   | | '__/ _ \/ _ \ __|
| |_\ \ | (_) | |_) | (_| | | /\__/ / |_) | (_| | | | | | | | | | | | (_| |   | | | |  __/  __/ |_
\____/_|\___/|_.__/ \__,_|_| \____/| .__/ \__,_|_| |_|_| |_|_|_| |_|\__, |   \_/_|  \___|\___|\__|
| |                               __/ |                        
|_|                              |___/                         

We are given an input file in which there are cities and transportation options (Airway, Railway, Highway) between the cities. After parsing and constructing a graph data structure, the following queries/commands must be satisfied:
- *Q1*: City1 City2 Ai Rj Hk | where i,j,k are unsigned integers. 
	- London Sydney A2 R1 H3. Start in London, use 2 Airways, 1 Railway, 3 Highways, and finish in Sydney. Return *one* path *in the given order*. 
- *Q2*: City1 City2 N | where N is an unsigned integer. 
	- Sydney Melbourne 3. Start from Sydney, pass 3 different cities, finish in Melbourne. Return *all possible paths*.
- *Q3*: City1 City2 Type | where type is the transportation type. 
	- Paris London Railway. Start in Paris and finish in London using *only the given transportation type*. Return *all possible paths*.
- *Bonus*: 
	- Do Q1 without the order constraint. 
	- Add an *ADD* command to add more cities and their transportation connections. 
