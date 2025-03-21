empty(N) :- N = node(-1, -1, 0, -1, -1, 0).
is_leaf(N) :- empty(E), N = node(_, _, _, E, E, _).

numMax(A,B,A) :- A >= B.
numMax(A,B,B) :- A < B.

find(N, _, _) :- empty(N), !, false.
find(N, X, Res) :- N = node(Key, _, _, _, R, _), X > Key, !, find(R, X, Res).
find(N, X, Res) :- N = node(Key, _, _, L, _, _), X < Key, !, find(L, X, Res).
find(N, _, Val) :- N = node(_, Val, _, _, _, _).

get_H(N, Res) :- N = node(_, _, Res, _, _, _).
get_sz(N, Res) :- N = node(_, _, _, _, _, Res).

new_node(N, L, R, Res) :- N = node(Key, Val, _, _, _, _), new_node(Key, Val, L, R, Res).
new_node(Key, Val, L, R, NewN) :- get_sz(L, SZL), get_sz(R, SZR), get_H(L, HL),
 get_H(R, HR), numMax(HL, HR, MaxH), HewH is MaxH + 1, NewSZ is SZL + SZR + 1, NewN = node(Key, Val, HewH, L, R, NewSZ).

smallLeft(A, Res) :- A = node(_, _, _, P, B, _), B = node(_, _, _, Q, R, _),
    new_node(A, P, Q, NewA),
    new_node(B, NewA, R, Res).

smallRight(B, Res) :- B = node(_, _, _, A, R, _), A = node(_, _, _, P, Q, _),
    new_node(B, Q, R, NewB),
    new_node(A, P, NewB, Res).

bigLeft(N, Res) :- N = node(_, _, _, L, R, _), smallRight(R, NewR), new_node(N, L, NewR, NewN), smallLeft(NewN, Res).
bigRight(N, Res) :- N = node(_, _, _, L, R, _), smallLeft(L, NewL), new_node(N, NewL, R, NewN), smallRight(NewN, Res).

do_balanced(N, N) :- N = node(_, _, _, L, R, _), get_H(L, HL), get_H(R, HR), Diff is HL - HR, Diff >= -1, Diff =< 1, !.
do_balanced(N, Res) :- N = node(_, _, _, L, R, _), get_H(L, HL), get_H(R, HR), 2 is HL - HR,
    L = node(_, _, _, LL, LR, _), get_H(LL, HLL), get_H(LR, HLR),  HLL >= HLR, !, smallRight(N, Res).
do_balanced(N, Res) :- N = node(_, _, _, L, R, _), get_H(L, HL), get_H(R, HR), 2 is HL - HR, !, bigRight(N, Res).
do_balanced(N, Res) :- N = node(_, _, _, L, R, _), get_H(L, HL), get_H(R, HR), -2 is HL - HR,
    R = node(_, _, _, RL, RR, _), get_H(RL, HRL), get_H(RR, HRR), HRR < HRL, !, bigLeft(N, Res).
do_balanced(N, Res) :- N = node(_, _, _, L, R, _), get_H(L, HL), get_H(R, HR), -2 is HL - HR, !, smallLeft(N, Res).


map_put(N, X, Val, Res) :- empty(N), !, new_node(X, Val, N, N, Res).
map_put(N, X, Val, Res) :- N = node(NKey, _, _, L, R, _), X == NKey, !, new_node(NKey, Val, L, R, Res).
map_put(N, X, Val, Res) :- N = node(NKey, _, _, L, R, _), X > NKey, !, map_put(R, X, Val, NewR),
    new_node(N, L, NewR, Res1), do_balanced(Res1, Res).
map_put(N, X, Val, Res) :- N = node(_, _, _, L, R, _), map_put(L, X, Val, NewL),
    new_node(N, NewL, R, Res1), do_balanced(Res1, Res).

map_get(N, Key, Val) :- find(N, Key, Val).

map_build([], Res) :- empty(Res), !.
map_build([(Key, Val) | T], Res) :- map_build(T, LastRes), map_put(LastRes, Key, Val, Res).


remove_list(N, _, Res, NLeaf) :- N = node(_, _, _, _, _, _), is_leaf(N), !, empty(Res), NLeaf = N.
remove_list(N, ListKey, Res, NLeaf) :- N = node(Key, _, _, L, R, _), ListKey > Key, empty(R), !, Res = L, NLeaf = N.
remove_list(N, ListKey, Res, NLeaf) :- N = node(Key, _, _, L, R, _), ListKey < Key, empty(L), !, Res = R, NLeaf = N.
remove_list(N, ListKey, Res, NLeaf) :- N = node(Key, _, _, L, R, _), ListKey > Key, !,
    remove_list(R, ListKey, NewR, NLeaf), new_node(N, L, NewR, Res1), do_balanced(Res1, Res).
remove_list(N, ListKey, Res, NLeaf) :- N = node(Key, _, _, L, R, _), ListKey < Key, !,
    remove_list(L, ListKey, NewL, NLeaf), new_node(N, NewL, R, Res1), do_balanced(Res1, Res).

map_remove(N, X, N) :- empty(N).
map_remove(N, X, Res) :- N = node(Key, _, _, L, R, _), X > Key, !, map_remove(R, X, NewR), new_node(N, L, NewR, Res1), do_balanced(Res1, Res).
map_remove(N, X, Res) :- N = node(Key, _, _, L, R, _), X < Key, !, map_remove(L, X, NewL), new_node(N, NewL, R, Res1), do_balanced(Res1, Res).
map_remove(N, X, Res) :- N = node(Key, _, _, _, _, _), X == Key, is_leaf(N), !, empty(Res).
map_remove(N, X, Res) :- N = node(Key, _, _, L, R, _), X == Key, empty(L), !, remove_list(R, Key, NewR, NLeaf),
    new_node(NLeaf, L, NewR, Res1), do_balanced(Res1, Res).
map_remove(N, _, Res) :- N = node(Key, _, _, L, R, _), remove_list(L, Key, NewL, NLeaf),
    new_node(NLeaf, NewL, R, Res1), do_balanced(Res1, Res).


get_res_ceil(Key, Val, null, Res2) :- !, Res2 = (Key, Val).
get_res_ceil(_, _, Res1, Res2) :- Res2 = Res1.

map_celing(N, _, null) :- empty(N), !.
map_celing(N, X, Res) :- N = node(Key, _, _, _, R, _), X > Key, !, map_celing(R, X, Res).
map_celing(N, X, Res) :- N = node(Key, Val, _, L, _, _), X < Key, !, map_celing(L, X, Res1), get_res_ceil(Key, Val, Res1, Res).
map_celing(N, X, Res) :- N = node(Key, Val, _, _, _, _), X == Key, Res = (Key, Val).


map_ceilingEntry(N, X, _) :- map_celing(N, X, Res1), Res1 == null, !, false.
map_ceilingEntry(N, X, Res) :- map_celing(N, X, Res).

remove_if(null, N, Res) :- !, Res = N.
remove_if(P, N, Res) :- P = (Key, _), map_remove(N, Key, Res).

map_removeCeiling(N, X, Res) :- map_celing(N, X, P), remove_if(P, N, Res).

map_headMapSize(N, _, 0) :- empty(N), !.
map_headMapSize(N, X, Res) :- N = node(Key, _, _, L, R, _), X > Key, !, map_headMapSize(R, X, Res1), get_sz(L, SZR), Res is Res1 + SZR + 1.
map_headMapSize(N, X, Res) :- N = node(Key, _, _, L, _, _), X < Key, !, map_headMapSize(L, X, Res).
map_headMapSize(N, X, Res) :- N = node(Key, _, _, L, _, _), X == Key, !, get_sz(L, SZ), Res is SZ.

map_tailMapSize(N, X, Res) :- map_headMapSize(N, X, Res1), get_sz(N, SZ), Res is SZ - Res1.