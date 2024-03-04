package expression.generic;

import java.math.BigInteger;

public class GenericTabulator implements Tabulator{

    private <T> Object[][][] getArray(String expStr, AbstractNumberOperations<T> operations, int x1, int x2, int y1, int y2, int z1, int z2) throws ParsingException {
        ExpressionParser<T> p = new ExpressionParser<>();
        ExpressionPart<T> exp = p.parse(expStr, operations);
        Object[][][] array = new Object[x2 - x1 + 1][y2 - y1 + 1][z2 - z1 + 1];
        for (int x = x1; x <= x2; ++x) {
            for (int y = y1; y <= y2; ++y) {
                for (int z = z1; z <= z2; ++z) {
                    try {
                        array[x - x1][y - y1][z - z1] = exp.evaluate(operations.convertInt(x), operations.convertInt(y), operations.convertInt(z));
                    } catch(RuntimeException e) {
                        array[x - x1][y - y1][z - z1] = null;
                    }
                }
            }
        }
        return array;
    }
    public Object[][][] tabulate(String mode, String expStr, int x1, int x2, int y1, int y2, int z1, int z2) throws ParsingException {
        return switch (mode) {
            case "i" -> getArray(expStr, new IntegerOperations(), x1, x2, y1, y2, z1, z2);
            case "d" -> getArray(expStr, new DoubleOperations(), x1, x2, y1, y2, z1, z2);
            case "bi" -> getArray(expStr, new BigIntegerOperations(), x1, x2, y1, y2, z1, z2);
            default -> throw new RuntimeException("invalid mode");
        };
    }

    public static class IntegerOperations extends AbstractNumberOperations<Integer> {
        public Integer add(Integer x, Integer y) {
            if ((x < 0 && y < 0) && ((Integer.MIN_VALUE - x) > y)) {
                throw new RuntimeException("overflow");
            }
            if ((x > 0 && y > 0) && ((Integer.MAX_VALUE - x) < y)) {
                throw new RuntimeException("overflow");
            }
            return x + y;
        }
        public Integer sub(Integer x, Integer y) {
            if ((x <= 0 && y >= 0) && ((Integer.MIN_VALUE - x) > -y)) {
                throw new RuntimeException("overflow");
            }
            if ((x >= 0 && y <= 0) && ((x - Integer.MAX_VALUE) > y)) {
                throw new RuntimeException("overflow");
            }
            return x - y;
        }

        public Integer mul(Integer x, Integer y) {
            int res = x * y;
            if (x != 0 && y != 0 && (res / y != x || res / x != y)) {
                throw new RuntimeException("overflow");
            }
            return res;
        }

        public Integer div(Integer x,  Integer y) {
            if (x == Integer.MIN_VALUE && y == -1) {
                throw new RuntimeException("overflow");
            }
            if (y == 0) {
                throw new RuntimeException("division by zero");
            }
            return x / y;
        }

        public Integer negate(Integer x) {
            if (x == Integer.MIN_VALUE) {
                throw new RuntimeException("overflow");
            }
            return -x;
        }

        public Integer convertInt(int x) {
            return x;
        }
    }

    public static class DoubleOperations extends AbstractNumberOperations<Double> {
        public Double add(Double x, Double y) {
            return x + y;
        }
        public Double sub(Double x, Double y) {
            return x - y;
        }

        public Double mul(Double x, Double y) {
            return x * y;
        }

        public Double div(Double x,  Double y) {
            return x / y;
        }

        public Double negate(Double x) {
            return -x;
        }

        public Double convertInt(int x) {
            return (double) x;
        }
    }

    public static class BigIntegerOperations extends AbstractNumberOperations<BigInteger> {
        public BigInteger add(BigInteger x, BigInteger y) {
            return x.add(y);
        }
        public BigInteger sub(BigInteger x, BigInteger y) {
            return x.subtract(y);
        }

        public BigInteger mul(BigInteger x, BigInteger y) {
            return x.multiply(y);
        }

        public BigInteger div(BigInteger x,  BigInteger y) {
            return x.divide(y);
        }

        public BigInteger negate(BigInteger x) {
            return x.negate();
        }

        public BigInteger convertInt(int x) {
            return BigInteger.valueOf(x);
        }
    }

}

