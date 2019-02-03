Frontier: bfs:fifo queue;  dfs:lifo stack;  greedy:ascending sortedset; A*:ascending sorted set;
node: created in getSucceessor, treenode
state/cell: part of a node, contains position, isgoal, iswall
action: get next expandable node up down left right
explored set: add a node when expanded it, keep record of total number
heuristic function: Manhatton distance(x+y)
main: get input, build all the node, record time for each algorithm

Handling	repeated	states	
• Initialize the	fron8er	using	the	star8ng	state	
• While	the	frontier	is	not	empty	
– Choose	a	frontier	node	according	to	search	strategy	and	take	
it	off	the	frontier	
– If	the	node	contains	the	goal	state,	return	solution	
– Else	expand	the	node	and	add	its	children	to	the	frontier	
• To	handle	repeated	states:	
– Everytime	you	expand	a	node,	add	that	state	to	the		
explored	set;	do	not	put	explored	states	on	the	frontier	again	
– Everytime	you	add	a	node	to	the	frontier,	check	whether	it	
already	exists	in	the	frontier	with	a	higher	path	cost,	and	if	
yes,	replace	that	node	with	the	new	one	



repeated states in 1.2:
a cell should also include the states of all goals. revisiting a cell with different goalStates is not counted as repeated.

searchNode has a state
everytime expand a cell

state has a cell and a goalState