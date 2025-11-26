package ec.com.bancointernacional.pruebatecnica.utils;

import com.peluware.domain.Order;
import com.peluware.domain.Pagination;
import com.peluware.domain.Sort;
import lombok.experimental.UtilityClass;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.function.BiFunction;

/**
 * Utilidades para adaptar los tipos de paginación y ordenamiento de Spring Data
 * a los tipos equivalentes usados en el dominio Peluware.
 * Este puente es útil cuando un servicio del dominio Peluware expone métodos que
 * aceptan {@link Pagination} y {@link Sort}, pero el controlador usa Spring Data.
 */
@UtilityClass
public final class PeluwareDomainAdapters {

    /**
     * Ejecuta una función del dominio Peluware que trabaja con {@link Pagination} y {@link Sort}
     * y devuelve el resultado envuelto en un {@link Page} de Spring Data.
     *
     * @param invoker  función del dominio que recibe paginación y ordenamiento Peluware
     * @param pageable paginación de Spring
     * @return página de Spring equivalente
     */
    public static <T> Page<T> adaptToSpringPage(
            Pageable pageable,
            BiFunction<Pagination, Sort, com.peluware.domain.Page<T>> invoker
    ) {
        var pagination = fromSpringPageable(pageable);
        var sort = fromSpringSort(pageable.getSort());
        var pelPage = invoker.apply(pagination, sort);

        return new PageImpl<>(
                pelPage.getContent(),
                pageable,
                pelPage.getTotalElements()
        );
    }

    /**
     * Convierte un {@link Pageable} de Spring a un {@link Pagination}
     * del dominio Peluware.
     */
    public static Pagination fromSpringPageable(Pageable pageable) {
        if (pageable.isUnpaged()) {
            return Pagination.unpaginated();
        }
        return Pagination.of(pageable.getPageNumber(), pageable.getPageSize());
    }

    /**
     * Convierte un {@link org.springframework.data.domain.Sort} de Spring
     * a un {@link Sort} del dominio Peluware.
     */
    public static Sort fromSpringSort(org.springframework.data.domain.Sort sort) {
        if (sort.isUnsorted()) {
            return Sort.unsorted();
        }
        return new Sort(sort.stream()
                .map(order -> new Order(
                        order.getProperty(),
                        order.isAscending()
                                ? Order.Direction.ASC
                                : Order.Direction.DESC
                ))
                .toList()
        );
    }
}
