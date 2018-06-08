package pe.com.foxsoft.ballartelyweb.batch.processor;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import pe.com.foxsoft.ballartelyweb.jpa.data.Provider;
import pe.com.foxsoft.ballartelyweb.jpa.data.ShippingHead;

public class ComprasItemProcessor implements ItemProcessor<ShippingHead, Provider>{

	private static final Logger logger = LoggerFactory.getLogger(ComprasItemProcessor.class);
	@Override
	public Provider process(ShippingHead input) throws Exception {
		logger.debug("Objeto compra: " + ReflectionToStringBuilder.reflectionToString(input));
		return null;
	}

}
