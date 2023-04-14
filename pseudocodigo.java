public abstract class Prenda {
    protected double precioBase;
    protected EstadoPrenda estadoPrenda;
    
    public abstract double precio();
}

// No es realmente necesario tener a los tipos de prendas heredando de Prenda
// Ya que no tienen ninguna caracteristica que las diferencie del resto mas allá del tipo
// Posible solución que favorece la simpleza: Tener un enum con los tipos de prenda y que exista en Prenda
public class Saco extends Prenda {
    public double precio() {
        return estadoPrenda.calcularPrecio(precioBase);
    }
}

public class Pantalon extends Prenda {
    public double precio() {
        return estadoPrenda.calcularPrecio(precioBase);
    }
}

public class Camisa extends Prenda {
    public double precio() {
        return estadoPrenda.calcularPrecio(precioBase);
    }
}


// Opté por esta opción por sobre la herencia ya que prioricé el factor de extensibilidad
// En un futura sería muy fácil agregar un nuevo estado
// Además opino que es poco cohesivo pensar que es responsabilidad de la prenda calcular como afecta su estado
// en su precio

interface EstadoPrenda {
    double calcularPrecio(double precioBase);
}

public class EstadoNueva implements EstadoPrenda {
    public double calcularPrecio(double precioBase) {
        return precioBase;
    }
}

public class EstadoPromocion implements EstadoPrenda {
    private double descuento;

    public EstadoPromocion(double descuento) {
        this.descuento = descuento;
    }

    public double calcularPrecio(double precioBase) {
        return precioBase - descuento;
    }
}

public class EstadoLiquidacion implements EstadoPrenda {
    public double calcularPrecio(double precioBase) {
        return precioBase * 0.5;
    }
}

// Agrego abstracción entre venta y Prenda que se encarga de agrupar a las mismas y hacer
// un reduce del precioTotal de la cantidad de prendas que conoce
public class Item {
    private int cantidad;
    private Prenda prenda;
    
    public double precioTotal() {
        return cantidad * prenda.precio();
    }
}

public class Venta {
    private Date fecha;
    private int cantCuotas;
    private MedioDePago medioDePago;
    private Item[] items;
    
    public double precioFinal() {
        double total = 0;
        for (Item item : items) {
            total += item.precioTotal();
        }
        total = medioDePago.modificarPrecio(total, cantCuotas);
        return total;
    }
}

interface MedioDePago {
    double modificarPrecio(double precioBase, int cantCuotas);
}

public class Efectivo implements MedioDePago {
    public double modificarPrecio(double precioBase, int cantCuotas) {
        return precioBase;
    }
}

public class Tarjeta implements MedioDePago {
    private double coeficienteRecargo;

    public Tarjeta(double coeficienteRecargo) {
        this.coeficienteRecargo = coeficienteRecargo;
    }

    public double modificarPrecio(double precioBase, int cantCuotas) {
        return precioBase + cantCuotas * coeficienteRecargo + 0.01 * precioBase;
    }
}