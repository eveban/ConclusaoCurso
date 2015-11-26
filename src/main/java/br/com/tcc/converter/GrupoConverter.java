package br.com.tcc.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.com.tcc.modelo.Grupo;
import br.com.tcc.repository.GrupoRepository;
import br.com.tcc.util.cdi.CDIServiceLocator;

@FacesConverter(forClass = Grupo.class)
public class GrupoConverter implements Converter {

	private GrupoRepository grupoRepository;

	public GrupoConverter() {
		grupoRepository = CDIServiceLocator.getBean(GrupoRepository.class);
	}

	@Override
	public Object getAsObject(FacesContext faces, UIComponent component, String value) {
		Grupo retorno = null;
		if (value != null) {
			Long id = new Long(value);
			retorno = grupoRepository.buscaPorId(id);
		}
		return retorno;

	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			Grupo Grupo = (Grupo) value;
			return Grupo.getId() == null ? null : Grupo.getId().toString();

		}

		return "";
	}

}
