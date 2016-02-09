package net.pixelstatic.novi.sprites;


public class LayerList{
	public Layer[] layers;
	static int maxsize = 6600;
	public int count, lastcount;
	boolean outofbounds;
	//this class is one big TODO
	/*
	public LayerList(){
		layers = new Layer[maxsize];
	}

	public LayerList(int size){
		layers = new Layer[size];
		maxsize = size;
	}

	public void add(Layer l){
		if(l == null || count >= maxsize - 1){
			if( !outofbounds) Home.log("--WARNING:-- Layer index out of bounds!");
			outofbounds = true;
			return;
		}
		layers[count] = l;
		count ++;
	}

	public synchronized void add(ArrayList<Layer> layers){
		for(Layer l : layers){
			add(l);
		}
	}

	public synchronized void sort(){
		Arrays.sort(layers, new Comparator<Layer>(){
			@Override
			public int compare(Layer o1, Layer o2){
				if(o1 == null && o2 == null){
					return 0;
				}
				if(o1 == null){
					return 1;
				}
				if(o2 == null){
					return -1;
				}
				return o1.compareTo(o2);
			}
		});
	}

	public synchronized void clear(){
		for(int i = 0;i < lastcount && i < maxsize;i ++){
			layers[i] = null;
		}
		lastcount = count;
		count = 0;
		outofbounds = false;
	}
	*/
}
