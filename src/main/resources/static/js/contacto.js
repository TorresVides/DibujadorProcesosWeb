/* ==========================================================================
   Validación del formulario Contáctenos.

   JavaScript propio, sin dependencias. El formulario lleva `novalidate`, de modo
   que el navegador no valida por su cuenta: las reglas de este archivo son las
   únicas que actúan en el cliente.

   Cuando todo es válido NO se llama a preventDefault: el formulario se envía de
   verdad a Spring, que vuelve a validar con Jakarta Validation antes de guardar.
   Este archivo mejora la experiencia de quien escribe; no es la garantía.
   ========================================================================== */

(function () {
	'use strict';

	var formulario = document.getElementById('formContacto');
	if (!formulario) {
		return;
	}

	/* Los límites vienen de los atributos data-* que renderiza Thymeleaf, no
	   escritos a mano aquí: así el servidor sigue siendo la única fuente. */
	function limite(id, atributo, porDefecto) {
		var elemento = document.getElementById(id);
		var valor = elemento ? parseInt(elemento.dataset[atributo], 10) : NaN;
		return isNaN(valor) ? porDefecto : valor;
	}

	var MIN_NOMBRE = limite('nombreCompleto', 'min', 3);
	var MIN_TELEFONO = limite('telefono', 'min', 7);
	var MAX_TELEFONO = limite('telefono', 'max', 15);
	var MIN_MENSAJE = limite('mensaje', 'min', 20);
	var MAX_MENSAJE = limite('mensaje', 'max', 400);

	/* Correo: algo antes de la @, un dominio sin espacios y al menos un punto
	   posterior a la @ con contenido detrás. */
	var CORREO = /^[^\s@]+@[^\s@.]+(\.[^\s@.]+)+$/;
	var SOLO_DIGITOS = /^\d+$/;

	/* Reglas por campo, en orden. Se evalúan hasta el primer fallo, de modo que
	   nunca se muestran dos mensajes a la vez sobre el mismo campo. */
	var REGLAS = {
		nombreCompleto: [
			{ probar: function (v) { return v.length > 0; },
			  mensaje: 'El nombre completo es obligatorio.' },
			{ probar: function (v) { return v.length >= MIN_NOMBRE; },
			  mensaje: 'El nombre completo debe tener al menos ' + MIN_NOMBRE + ' caracteres.' }
		],
		correo: [
			{ probar: function (v) { return v.length > 0; },
			  mensaje: 'El correo electrónico es obligatorio.' },
			{ probar: function (v) { return CORREO.test(v); },
			  mensaje: 'Escribe un correo válido: debe contener @ y un punto después de la @.' }
		],
		telefono: [
			{ probar: function (v) { return v.length > 0; },
			  mensaje: 'El teléfono es obligatorio.' },
			{ probar: function (v) { return SOLO_DIGITOS.test(v); },
			  mensaje: 'El teléfono solo admite números.' },
			{ probar: function (v) { return v.length >= MIN_TELEFONO; },
			  mensaje: 'El teléfono debe tener al menos ' + MIN_TELEFONO + ' dígitos.' },
			{ probar: function (v) { return v.length <= MAX_TELEFONO; },
			  mensaje: 'El teléfono no puede superar los ' + MAX_TELEFONO + ' dígitos.' }
		],
		asunto: [
			{ probar: function (v) { return v !== ''; },
			  mensaje: 'Selecciona un motivo de contacto.' }
		],
		mensaje: [
			{ probar: function (v) { return v.length > 0; },
			  mensaje: 'El mensaje es obligatorio.' },
			{ probar: function (v) { return v.length >= MIN_MENSAJE; },
			  mensaje: 'El mensaje debe tener al menos ' + MIN_MENSAJE + ' caracteres.' },
			{ probar: function (v) { return v.length <= MAX_MENSAJE; },
			  mensaje: 'El mensaje no puede superar los ' + MAX_MENSAJE + ' caracteres.' }
		]
	};

	var CAMPOS = Object.keys(REGLAS);
	var tocados = {};

	function control(nombre) {
		return document.getElementById(nombre);
	}

	function contenedor(nombre) {
		return document.querySelector('[data-campo="' + nombre + '"]');
	}

	function valorLimpio(nombre) {
		var elemento = control(nombre);
		return elemento ? elemento.value.trim() : '';
	}

	function primerError(nombre) {
		var valor = valorLimpio(nombre);
		var reglas = REGLAS[nombre];
		for (var i = 0; i < reglas.length; i++) {
			if (!reglas[i].probar(valor)) {
				return reglas[i].mensaje;
			}
		}
		return null;
	}

	function pintar(nombre, error) {
		var caja = contenedor(nombre);
		var elemento = control(nombre);
		var aviso = document.getElementById('error-' + nombre);
		if (!caja || !elemento) {
			return;
		}

		caja.classList.toggle('campo--error', Boolean(error));
		caja.classList.toggle('campo--valido', !error);

		if (error) {
			elemento.setAttribute('aria-invalid', 'true');
		} else {
			elemento.removeAttribute('aria-invalid');
		}

		if (aviso) {
			aviso.textContent = error || '';
			aviso.hidden = !error;
		}
	}

	function validar(nombre) {
		var error = primerError(nombre);
		pintar(nombre, error);
		return error === null;
	}

	/* Contador del mensaje: por debajo del mínimo indica cuántos caracteres
	   faltan; a partir de ahí, cuántos se llevan sobre el máximo. */
	function actualizarContador() {
		var contador = document.getElementById('contador-mensaje');
		var elemento = control('mensaje');
		if (!contador || !elemento) {
			return;
		}

		var longitud = elemento.value.trim().length;
		contador.classList.remove('campo__contador--insuficiente', 'campo__contador--limite');

		if (longitud < MIN_MENSAJE) {
			var faltan = MIN_MENSAJE - longitud;
			contador.textContent = faltan === 1
				? 'Falta 1 carácter para el mínimo.'
				: 'Faltan ' + faltan + ' caracteres para el mínimo.';
			contador.classList.add('campo__contador--insuficiente');
			return;
		}

		contador.textContent = longitud + ' de ' + MAX_MENSAJE + ' caracteres.';
		if (longitud >= MAX_MENSAJE * 0.9) {
			contador.classList.add('campo__contador--limite');
		}
	}

	CAMPOS.forEach(function (nombre) {
		var elemento = control(nombre);
		if (!elemento) {
			return;
		}

		/* El select se valida en cuanto cambia: no tiene sentido esperar al blur. */
		if (elemento.tagName === 'SELECT') {
			elemento.addEventListener('change', function () {
				tocados[nombre] = true;
				validar(nombre);
			});
			return;
		}

		elemento.addEventListener('blur', function () {
			tocados[nombre] = true;
			validar(nombre);
		});

		/* Mientras se escribe por primera vez no se muestran errores; solo se
		   revalida si el campo ya fue tocado. */
		elemento.addEventListener('input', function () {
			if (tocados[nombre]) {
				validar(nombre);
			}
		});
	});

	/* El teléfono descarta lo que no sea un dígito al escribir o al pegar, para
	   que el campo nunca llegue a contener letras. */
	var telefono = control('telefono');
	if (telefono) {
		telefono.addEventListener('input', function () {
			var limpio = telefono.value.replace(/\D/g, '');
			if (limpio !== telefono.value) {
				telefono.value = limpio;
			}
		});
	}

	var mensaje = control('mensaje');
	if (mensaje) {
		mensaje.addEventListener('input', actualizarContador);
		actualizarContador();
	}

	formulario.addEventListener('submit', function (evento) {
		var invalidos = CAMPOS.filter(function (nombre) {
			tocados[nombre] = true;
			return !validar(nombre);
		});

		if (invalidos.length === 0) {
			/* Sin preventDefault: el POST llega a Spring, que valida y persiste. */
			return;
		}

		evento.preventDefault();

		var primero = control(invalidos[0]);
		if (primero) {
			primero.focus();
			if (typeof primero.scrollIntoView === 'function') {
				primero.scrollIntoView({ block: 'center' });
			}
		}
	});

}());
