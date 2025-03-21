

do_thing(I, _) :- myused(I), !.
do_thing(I, Step) :- assert(min_prime_table(I, Step)), assert(myused(I)).

do_loop(Max, _, I) :- I > Max.
do_loop(Max, Step, I) :- I =< Max,!, do_thing(I, Step),!, NewI is I + Step,!, do_loop(Max, Step, NewI).

do_thing2(_, I) :- myused(I), !.
do_thing2(Max, I) :- do_loop(Max, I, I).

get_primes(Max, I) :- I > Max, !.
get_primes(Max, I) :- I1 is I + 1, do_thing2(Max, I),  get_primes(Max, I1).
init(N) :- get_primes(N, 2).



prime(N) :- min_prime_table(N, R), !, R is N.
composite(N) :- \+ prime(N).

is_sort([]).
is_sort([_]).
is_sort([R1, R2 | T]) :- T1 = [R2 | T], R1 =< R2, is_sort(T1).

is_prime_list([]).
is_prime_list([R]) :- !, prime(R).
is_prime_list([R | T]) :- prime(R), is_prime_list(T).

get_multiply_list([], 1).
get_multiply_list([R], R) :- !.
get_multiply_list([V | T], R) :- get_multiply_list(T, R1), R is R1 * V.

prime_divisors(1, []) :- !.
prime_divisors(N, List) :- var(List), !, min_prime_table(N, R), !, N1 is div(N, R), prime_divisors(N1, List1), List = [R | List1].
prime_divisors(N, List) :- is_sort(List), is_prime_list(List), get_multiply_list(List, N1), N is N1.

get_count_pair(V, [], [V, 0], []) :- !.
get_count_pair(V, [F | T], Pair, Ost) :- \+ F == V, Pair = [V, 0], Ost = [F | T], !.
get_count_pair(V, [_ | T], Pair, Ost) :- get_count_pair(V, T, [PairV, PairC], Ost1), NewPairC is PairC + 1, Pair = [PairV, NewPairC], Ost = Ost1.

get_count_vector([], []) :- !.
get_count_vector([V | T], Res) :- get_count_pair(V, [V | T], Pair1, Ost1), get_count_vector(Ost1, Res1), Res = [Pair1 | Res1].

get_pow(_, 0, 1) :- !.
get_pow(V, Pow, Res) :- Pow1 is Pow - 1, get_pow(V, Pow1, Res1), Res is Res1 * V.
numMin(A,B,A) :- A =< B.
numMin(A,B,B) :- A > B.
numMax(A,B,A) :- A >= B.
numMax(A,B,B) :- A < B.

get_gcd([], [], _, 1) :- !.
get_gcd([], [[VR, CR] | TR], Func, Res) :- get_gcd([], TR, Func, Res1), G =.. [Func, CR, 0, PowC], call(G), get_pow(VR, PowC, Pow), Res is Res1 * Pow, !.
get_gcd([[VL, CL] | TL], [], Func, Res) :- get_gcd(TL, [], Func, Res1), G =.. [Func, CL, 0, PowC], call(G), get_pow(VL, PowC, Pow), Res is Res1 * Pow, !.
get_gcd([[VL, CL] | TL], [[VR, CR] | TR], Func, Res) :- VL < VR, get_gcd(TL, [[VR, CR] | TR], Func, Res1), G =.. [Func, CL, 0, PowC], call(G),
    get_pow(VL, PowC, Pow), Res is Res1 * Pow, !.
get_gcd([[VL, CL] | TL], [[VR, CR] | TR], Func, Res) :- VL > VR, get_gcd([[VL, CL] | TL], TR, Func, Res1), G =.. [Func, CR, 0, PowC], call(G),
    get_pow(VR, PowC, Pow), Res is Res1 * Pow, !.
get_gcd([[VL, CL] | TL], [[VR, CR] | TR], Func, Res) :- VL == VR, get_gcd(TL, TR, Func, Res1), G =.. [Func, CL, CR, PowC], call(G), get_pow(VL, PowC, Pow), Res is Res1 * Pow.

abs_func(A, 0, Func, A) :- !.
abs_func(0, B, Func, B) :- !.
abs_func(A, B, Func, Res) :- prime_divisors(A, ListA), prime_divisors(B, ListB), get_count_vector(ListA, PA), get_count_vector(ListB, PB),
    get_gcd(PA, PB, Func, Res).

gcd(A, B, Res) :- abs_func(A, B, numMin, Res).
lcm(A, B, Res) :- abs_func(A, B, numMax, Res).
