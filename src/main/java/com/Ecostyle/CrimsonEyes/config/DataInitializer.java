package com.Ecostyle.CrimsonEyes.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.Ecostyle.CrimsonEyes.model.Producto;
import com.Ecostyle.CrimsonEyes.repository.ProductoRepository;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private ProductoRepository productoRepository;

    @Override
    public void run(String... args) throws Exception {
        // Verificar si los productos ya existen
        if (productoRepository.count() == 0) {
            System.out.println("[DataInitializer] Cargando productos predeterminados...");

            // Crear los 6 productos
            Producto p1 = new Producto(
                1,
                "Lentes de Lectura",
                30000,
                "Lentes ideales para lecturas prolongadas y de alto requerimiento, con filtro anti-reflejo y protección contra luz azul",
                15,
                "Lectura"
            );

            Producto p2 = new Producto(
                2,
                "Lentes Redondos",
                50000,
                "Estilo minimalista y con aire de nostalgia retro, evocando un satirico suspiro de los 70's",
                10,
                "Redondos"
            );

            Producto p3 = new Producto(
                3,
                "Lentes Futuristas",
                15000,
                "Inspirados en la estética cyberpunk, simulando un visor de poligonos que devienen. Audaces, vanguardistas y diseñados para destacar. Tecnología y moda se funden en una sola mirada.",
                22,
                "Aceleracionista"
            );

            Producto p4 = new Producto(
                4,
                "Lentes Armani VE4361",
                30000,
                "Elegancia en cada detalle. Combinando diseño clásico italiano con materiales de alta gama. Su estructura ligera y acabado metálicos reflejan distinción y elegancia. Perfectos para entornos formales o un look urbano sofisticado.",
                15,
                "Armani"
            );

            Producto p5 = new Producto(
                5,
                "Lentes Rave's",
                30000,
                "Lentes de forma cuadrada y con cristales espejados o de colores neón, pensados para brillar bajo luces estroboscópicas. Ideal para camuflarse en la cultura electrónica.",
                15,
                "IDM"
            );

            Producto p6 = new Producto(
                6,
                "Lentes Filtro",
                30000,
                "Diseñados para la era digital, lentes que protegen tus ojos del brillo de pantallas y dispositivos. Diseño discreto y moderno que los hace ideales para uso diario, combinando salud visual con estilo. Ideal para reducir la fatiga ocular y aportan una estética limpia",
                15,
                "Filtros azul"
            );

            // Guardar todos los productos
            productoRepository.save(p1);
            productoRepository.save(p2);
            productoRepository.save(p3);
            productoRepository.save(p4);
            productoRepository.save(p5);
            productoRepository.save(p6);

<<<<<<< HEAD
            System.out.println("[DataInitializer] productos cargados exitosamente");
=======
            System.out.println("[DataInitializer] ✅ 6 productos cargados exitosamente");
>>>>>>> f4db997a54f17682b088090e416eaf0312c8a11a
        } else {
            System.out.println("[DataInitializer] Los productos ya existen en la BD, saltando inicialización");
        }
    }
}

