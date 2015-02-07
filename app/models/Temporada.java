package models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.*;

@Entity
public class Temporada implements Comparable<Temporada>{
	@Transient
	private int completa = 1;
	@Transient
	private int incompleta = 0;
	@Transient
	private int totalIncompleta = -1;
	
	@Id
	@GeneratedValue
	private Long id;
	
	@Column
	private int numero;
	@Column
	private int status;
	@Column
	private int qtdEpisodios;
	
	@ManyToOne(cascade=CascadeType.ALL)
	private Serie serie;
	
	@OneToMany(cascade=CascadeType.ALL)
	@JoinColumn(name="TEMP_EPS")
	private List<Episodio> episodios;

	public Temporada() {
		status = -1;
		episodios = new ArrayList<>();
		qtdEpisodios = episodios.size();
	}
	
	public Temporada(int num, Serie serie){
		this();
		this.numero = num;
		this.serie = serie;
	}
	
	public void addEpisodio(Episodio ep){
		episodios.add(ep);
		qtdEpisodios++;
	}

	public Long getId() {
		return id;
	}

	private void setId(Long id) {
		this.id = id;
	}

	public int getNumero() {
		return numero;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}

	public List<Episodio> getEpisodios() {
		return episodios;
	}

	public void setEpisodios(List<Episodio> episodios) {
		this.episodios = episodios;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Serie getSerie() {
		return serie;
	}

	public void setSerie(Serie serie) {
		this.serie = serie;
	}
	
	public Episodio getEpisodio(int num){
		Collections.sort(episodios);
		return episodios.get(num-1);
	}

	public int getQtdEpisodios() {
		return qtdEpisodios;
	}

	public void setQtdEpisodios(int qtdEpisodios) {
		this.qtdEpisodios = qtdEpisodios;
	}
	
	public String getProximoEpisodio(){
		String result = episodios.get(0).getNumero() + " - " + episodios.get(0).getNome();
		
		for (int i = qtdEpisodios-1; i >= 0; i--) {
			if(episodios.get(i).isAssistido()){
				if(i < qtdEpisodios-2){
					result = episodios.get(i+1).getNumero() + " - " + episodios.get(i+1).getNome();
				}
			}
		}
		
		if(episodios.get(qtdEpisodios-1).isAssistido()){
			result = "Você já assistiu ao último episódio desta temporada.";
		}
		
		return result;
	}
	
	public int verificaStatus(){
		int cont = 0;
		
		for (Episodio ep : episodios) {
			if(ep.isAssistido()){
				cont += 1;
			}
		}
		
		if(cont == 0) {
			status = totalIncompleta;
		}
		if(cont > 0) {
			status = incompleta;
		}
		if(cont == qtdEpisodios || episodios.get(qtdEpisodios-1).isAssistido()) {
			status = completa;
		}
		
		return status;
	}

	@Override
	public int compareTo(Temporada outraTemporada) {
		if (this.getNumero()>outraTemporada.getNumero()){
			return 1;
		} else if (this.getNumero()<outraTemporada.getNumero()){
			return -1;
		}
		return 0;
	}	
}
