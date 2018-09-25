package pe.com.foxsoft.ballartelyweb.batch.processor;

import java.util.Date;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import pe.com.foxsoft.ballartelyweb.jpa.data.GuideHead;
import pe.com.foxsoft.ballartelyweb.spring.util.Constantes;

public class ComprasItemProcessor implements ItemProcessor<GuideHead, GuideHead>{

	private static final Logger logger = LoggerFactory.getLogger(ComprasItemProcessor.class);
	@Override
	public GuideHead process(GuideHead input) throws Exception {
		logger.debug("Objeto compra: " + ReflectionToStringBuilder.reflectionToString(input));
		if(Constantes.GUIDE_TYPE_BUY.equals(input.getGuideType())
				&& Constantes.STATUS_PRODUCT_FRESH.equals(input.getGuideStatus())
				&& input.getEmissionDate().before(new Date())) {
			input.setGuideStatus(Constantes.STATUS_PRODUCT_COLD);
		}
		return input;
	}

}
