package l33tD33r.app.database.query;

public enum ExpressionNodeType {
	Value,
	Field,
	Column,
    Parameter,
	Equal,
    Not,
    If,
	GreaterThan,
	LessThan,
	GreaterThanOrEqual,
	LessThanOrEqual,
	And,
	Or,
	Group,
	SetCount,
	SetFilter,
	Add,
	Subtract,
	Multiply,
	Divide,
	Modulo,
	Power
}
