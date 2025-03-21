:- load_library('alice.tuprolog.lib.DCGLibrary').

expr_term(variable(Name), Name) :- atom(Name).
expr_term(const(Value), Value) :- number(Value).
expr_term(operation(Op, A, B), R) :- R =.. [Op, AT, BT], expr_term(A, AT), expr_term(B, BT).
expr_term(operation(Op, A), R) :- R =.. [Op, AT], expr_term(A, AT).
expr_term(operation(Op, A, B, C), R) :- R =.. [Op, AT, BT, CT], expr_term(A, AT), expr_term(B, BT), expr_term(C, CT).

expr_text(E, S) :- ground(E), expr_term(E, T), text_term(S, T).
expr_text(E, S) :-   atom(S), text_term(S, T), expr_term(E, T).


lookup(K, [(K, V) | _], V).
lookup(K, [_ | T], V) :- lookup(K, T, V).

operation(op_add, A, B, R) :- R is A + B.
operation(op_subtract, A, B, R) :- R is A - B.
operation(op_multiply, A, B, R) :- R is A * B.
operation(op_divide, A, B, R) :- AD is A * 1.0, BD is B * 1.0, R is AD / BD.
operation(op_bitand, A, B, R) :- R is A /\ B.
operation(op_bitor, A, B, R) :- R is A \/ B.
operation(op_bitxor, A, B, R) :- R is ((\A) /\ B) \/ ((\B) /\ A).
operation(op_bitif, C, T, _, R) :- NumC is round(C), 1 is mod(NumC, 2), !, R = T.
operation(op_bitif, _, _, F, R) :- R = F.
operation(op_bitmux, S, V0, V1, R) :- R is ((\S) /\ V0) \/ (S /\ V1).


operation(op_negate, A, R) :- R is -A.
operation(op_bitnot, A, R) :- R is \A.

toLowerCase('x', 'x').
toLowerCase('X', 'x').
toLowerCase('y', 'y').
toLowerCase('Y', 'y').
toLowerCase('z', 'z').
toLowerCase('Z', 'z').

evaluate(const(Value), _, Value).
evaluate(variable(Name), Vars, R) :- atom_chars(Name, [C | _]), toLowerCase(C, LowC), lookup(LowC, Vars, R).

evaluate(operation(Op, A, B, C), Vars, R) :-
    evaluate(A, Vars, AV),
    evaluate(B, Vars, BV),
    evaluate(C, Vars, CV),
    operation(Op, AV, BV, CV, R).
evaluate(operation(Op, A, B), Vars, R) :-
    evaluate(A, Vars, AV),
    evaluate(B, Vars, BV),
    operation(Op, AV, BV, R).
evaluate(operation(Op, A), Vars, R) :-
    evaluate(A, Vars, AV),
    operation(Op, AV, R).

nonvar(V, _) :- var(V).
nonvar(V, T) :- nonvar(V), call(T).

digits_p([]) --> [].
digits_p([H | T]) -->
  { member(H, ['0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '.', '-'])},
  [H],
  digits_p(T).

variable_p([]) --> [].
variable_p([H | T]) -->
    { member(H, ['x', 'X', 'y', 'Y', 'z', 'Z'])},
    [H],
    variable_p(T).

op_p(op_add) --> ['+'].
op_p(op_subtract) --> ['-'].
op_p(op_multiply) --> ['*'].
op_p(op_divide) --> ['/'].
op_p(op_bitand) --> ['&'].
op_p(op_bitor) --> ['|'].
op_p(op_bitxor) --> ['^'].
unar_op_r(op_negate) --> ['n', 'e', 'g', 'a', 't', 'e'].
unar_op_r(op_bitnot) --> ['~'].

char_end([C], C) :- !, false.
char_end([_], _) :- true.
char_end([_ | T], C) :- char_end(T, C).

spaces_p([]) --> [].
spaces_p([H | T]) -->
    [H],
    {H == ' '},
    spaces_p(T).

spaces(B, D) --> {nonvar(B)}, D.
spaces(B, _) --> {var(B)},
    spaces_p(_).

expr_p(variable(Name)) -->
  { nonvar(Name, atom_chars(Name, Chars)) },
  variable_p(Chars),
  { Chars = [_ | _], atom_chars(Name, Chars) }.


expr_p(const(Value)) -->
  { nonvar(Value, number_chars(Value, Chars)) },
  digits_p(Chars),
  { Chars = [_ | _], char_end(Chars, '.'), char_end(Chars, '-'),  number_chars(Value, Chars) }.


expr_p(operation(Op, A, B)) -->
    {nonvar(B, T = null)},
    ['('],
    spaces(T, []),
    expr_p(A),
    spaces(T, [' ']),
    op_p(Op),
    spaces(T, [' ']),
    expr_p(B),
    spaces(T, []),
    [')'].

expr_p(operation(Op, A)) -->
    {nonvar(A, T = null)},
    unar_op_r(Op),
    spaces(T, [' ']),
    expr_p(A).


tripleExp_p(Char, C, T, F) -->
    {nonvar(C, V = null)},
    ['('],
    spaces(V, []),
    expr_p(C),
    spaces(V, [' ']),
    [Char],
    spaces(V, [' ']),
    expr_p(T),
    spaces(V, [' ']),
    [':'],
    spaces(V, [' ']),
    expr_p(F),
    spaces(V, []),
	[')'].

expr_p(operation(op_bitif, C, T, F)) -->
    tripleExp_p('?', C, T, F).

expr_p(operation(op_bitmux, C, T, F)) -->
    tripleExp_p('¿', C, T, F).


start_p(A) -->
	{nonvar(A, Exp = [])},
    {nonvar(A, Exp1 = [])},
    spaces_p(Exp),
    expr_p(A),
    spaces_p(Exp1).

infix_str(E, A) :- ground(E), phrase(start_p(E), C), atom_chars(A, C).
infix_str(E, A) :-   atom(A), atom_chars(A, C), phrase(start_p(E), C).




