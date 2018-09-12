package pe.com.foxsoft.ballartelyweb.jpa.data;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * The persistent class for the transport database table.
 * 
 */
@Entity
@Table(name = "provider_transport")
public class ProviderTransport implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private Integer id;
	
	//bi-directional many-to-one association to Provider
	@ManyToOne
	@JoinColumn(name="provider_id")
	private Provider provider;
	
	//bi-directional many-to-one association to Provider
	@ManyToOne
	@JoinColumn(name="transport_id")
	private Transport transport;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Provider getProvider() {
		return provider;
	}

	public void setProvider(Provider provider) {
		this.provider = provider;
	}

	public Transport getTransport() {
		return transport;
	}

	public void setTransport(Transport transport) {
		this.transport = transport;
	}
	
}
