package it.polito.tdp.nobel.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.polito.tdp.nobel.db.EsameDAO;

public class Model {

	private List<Esame> partenza;
	private Set<Esame> soluzioneMigliore; // così da non doverla inserire negli argomenti della funzione ricorsiva
	private double mediaSoluzioneMigliore;
	
	public Model() {
		EsameDAO dao = new EsameDAO();
		this.partenza = dao.getTuttiEsami();
	}
	
	public Set<Esame> calcolaSottoinsiemeEsami(int numeroCrediti) {
		Set<Esame> parziale = new HashSet<Esame>();
		//meglio inizializzarla qui tale che ad ogni uso si azzera
		soluzioneMigliore= new HashSet<>();
		mediaSoluzioneMigliore=0;
		
		//cerca(parziale, 0, numeroCrediti);
		cerca2(parziale, 0, numeroCrediti);
		
		return soluzioneMigliore;	
	}

	/*Complessità N!*/
	private void cerca(Set<Esame> parziale, int L, int m) {
		// casi terminali
		
		int crediti = sommaCrediti(parziale);
		
		if(crediti>m) {
			return;
		}else if(crediti==m) {
			double media = calcolaMedia(parziale);
			if(media>mediaSoluzioneMigliore) {
				soluzioneMigliore=new HashSet<>(parziale);
				mediaSoluzioneMigliore=media;
			}
			return;
		}
		
		//1)  L=N--> non ci sono più esami da aggiungere
		if(L== partenza.size()) {
			return;
		}
		
		//genero i sotto_problemi
		for (Esame e: partenza) {
			if(!parziale.contains(e)) {
				parziale.add(e);
				cerca(parziale, L+1,m);
				parziale.remove(e); //backtracking
			}
		}
	}

	//Complessità di 2^N
	private void cerca2(Set<Esame> parziale, int L, int m) {
		// casi terminali
		
		int crediti = sommaCrediti(parziale);
				
		if(crediti>m) {
			return;
		}else if(crediti==m) {
			double media = calcolaMedia(parziale);
			if(media>mediaSoluzioneMigliore) {
				soluzioneMigliore=new HashSet<>(parziale);
				mediaSoluzioneMigliore=media;
			}
			return;
		}
				
		//1)  L=N--> non ci sono più esami da aggiungere
		if(L== partenza.size()) {
			return;
		}
		
		//generazione sotto_problemi
		//partenza[L] è da aggiungere oppure no? Provo entrambe
		parziale.add(partenza.get(L));
		cerca2(parziale,L+1,m);
		parziale.remove(partenza.get(L));
		cerca2(parziale,L+1,m);
	}
	
	public double calcolaMedia(Set<Esame> esami) {
		
		int crediti = 0;
		int somma = 0;
		
		for(Esame e : esami){
			crediti += e.getCrediti();
			somma += (e.getVoto() * e.getCrediti());
		}
		
		return somma/crediti;
	}
	
	public int sommaCrediti(Set<Esame> esami) {
		int somma = 0;
		
		for(Esame e : esami)
			somma += e.getCrediti();
		
		return somma;
	}

}
