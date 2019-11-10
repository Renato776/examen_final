package com.edd;

public class Arbol {
    Alumno[] contenido;
    int size;
    int filled_nodes;
    public Arbol(){
        size = 100;
        filled_nodes = 0;
        contenido = new Alumno[size];
    }
    public void insert(Alumno alumno){
        if(contenido[0] == null){
            //Empty tree, we set the root:
            contenido[0] = alumno;
            filled_nodes = 1;
            return;
        }
        insert(0,alumno);
        filled_nodes++;
    }

    int height(int node) {
        if (contenido[node] == null)
            return 0;
        return contenido[node].height;
    }
    int max(int a, int b) {
        return (a > b) ? a : b;
    }

    void rightRotate(int y) {
        //1) Create a copy of the current values before any modification:
        Alumno[] og = get_backup();
        //1.5) Get all the needed addresses:
        int x = left(y);
        int T3 = right(y);
        int T1 = left(x);
        int T2 = right(x);
        //2) Swap the pure values of x and y:
        var old = contenido[y];
        contenido[y] = contenido[x];
        contenido[x] = old;
        //3) Move y to the right of x:
        update(left(y),right(y));
        og = check_backup(og);
        //4) Move T1 from OG to left(y):
        move(og,T1,left(y));
        og = check_backup(og);
        //5) Move T2 from OG to left(right(y))
        move(og,T2,left(right(y)));
        og = check_backup(og);
        //6)Move T3 from OG to right(right(y))
        move(og,T3,right(right(y)));
        og = check_backup(og);
        //7)Update heigths:
        if(contenido[right(y)]!=null)contenido[right(y)].height = max(height(left(right(y))), height(right(right(y)))) + 1;
        if(contenido[y]!=null)contenido[y].height = max(height(left(y)), height(right(y))) + 1;

        /*
        if(contenido[y]!=null)contenido[y].height = max(height(left(y)), height(right(y))) + 1;
        if(contenido[x]!=null)contenido[x].height = max(height(left(x)), height(right(x))) + 1;
        if(contenido[right(y)]!=null)contenido[right(y)].height = max(height(left(right(y))), height(right(right(y)))) + 1;
        */
    }
    void leftRotate(int x) {
        //1) Create a copy of the current values before any modification:
        Alumno[] og = get_backup();
        //1.5) Get all the needed addresses:
        int y = right(x);
        int T3 = right(y);
        int T1 = left(x);
        int T2 = left(y);
        //2) Swap the pure values of x and y:
        var old = contenido[x];
        contenido[x] = contenido[y];
        contenido[y] = old;
        //3) Move x to the right of x:
        update(right(x),left(x));
        og = check_backup(og);
        //4) Move T3 from OG to right(x):
        move(og,T3,right(x));
        og = check_backup(og);
        //5) Move T1 from OG to left(left(x))
        move(og,T1,left(left(x)));
        og = check_backup(og);
        //6)Move T2 from OG to right(left(x))
        move(og,T2,right(left(x)));
        og = check_backup(og);
        //7)Update heigths:
        if(contenido[left(x)]!=null)contenido[left(x)].height = max(height(left(left(x))), height(right(left(x)))) + 1;
        if(contenido[x]!=null)contenido[x].height = max(height(left(x)), height(right(x))) + 1;
    }
    int getBalance(int N) {
        if (contenido[N] == null)
            return 0;

        return height(left(N)) - height(right(N));
    }
    public Alumno[] get_backup(){
        var res = new Alumno[size];
        int i = 0;
        for (Alumno al: contenido) {
            res[i] = al;
            i++;
        }
        return res;
    }
    void delete(int node){
        if(contenido[node]==null)return;
        contenido[node] = null;
        delete(left(node));
        delete(right(node));
    }
    public void move(Alumno[] og, int target, int position){
        //This method moves the target node in og to the desired position in the content.
        //This also moves any subtree there might be in the target children.
        if(contenido[position]!=null){
            //There's already something in the desired position. Let's delete it first:
            delete(position);
        }
        //Alright, now let's start the moving:
        contenido[position] = og[target];
        if(og[left(target)]!=null){
            //It has a left subtree, let's move it as well:
            move(og,left(target),left(position));
        }
        if(og[right(target)]!=null){
            //let's move the right subtree as well:
            move(og,right(target),right(position));
        }
    }

    public void update(int old_index, int new_index){
        //This method moves the subtree stored in old index to a new position given by the new_index. If there's already a subtree in new_index it is deleted.
        if(contenido[new_index] != null){
            //Its already occupied, we must first delete it:
            delete(new_index);
        }
        contenido[new_index] = contenido[old_index];
        //Free the old one:
        contenido[old_index] = null;
        int x = left(old_index);
        int y = right(old_index);
        if(contenido[x]!=null){
            //There is a left subtree, therefore it must be moved as well:
            update(x,left(new_index));
        }
        if(contenido[y]!=null){
            update(y,right(new_index));
        }
    }
    public void resize(){
        size = size*2;
        var old = contenido;
        contenido = new Alumno[size];
        int i = 0;
        for (Alumno al: old) {
            contenido[i] = al;
            i++;
        }
    }
    private void insert(int node, Alumno alumno){
        /* 1.  Perform the normal BST insertion */
        if (contenido[node] == null){
            contenido[node] = alumno;
            return;
        }

        if (alumno.carnet < contenido[node].carnet)
            insert(left(node),alumno);
        else if (alumno.carnet > contenido[node].carnet)
            insert(right(node), alumno);
        else // Duplicate keys not allowed
            return;

        /* 2. Update height of this ancestor node */
        contenido[node].height = 1 + max(height(left(node)),
                height(right(node)));

        /* 3. Get the balance factor of this ancestor
              node to check whether this node became
              unbalanced */
        int balance = getBalance(node);

        // If this node becomes unbalanced, then there
        // are 4 cases Left Left Case
        if (balance > 1 && alumno.carnet < contenido[left(node)].carnet) {
            rightRotate(node);
            return;
        }
        // Right Right Case
        if (balance < -1 && alumno.carnet > contenido[right(node)].carnet) {
            leftRotate(node);
            return;
        }

        // Left Right Case
        if (balance > 1 && alumno.carnet >contenido[left(node)].carnet) {
            leftRotate(left(node));
            rightRotate(node);
            return;
        }

        // Right Left Case
        if (balance < -1 && alumno.carnet < contenido[right(node)].carnet) {
            rightRotate(right(node));
            leftRotate(node);
            return;
        }

        return;
    }
    public int get_parent(int index){
        if(index == 0) return 0;
        double res = (index-1)/2;
        return (int) Math.floor(res);
    }
    public int left(int index){
        int answer = 2*index + 1;
        if(answer >= size){
            resize();
        }
        return answer;
    }
    public int right(int index){
        int answer = 2*index + 2;
        if(answer >= size){
            resize();
        }
        return answer;
    }
    public void simple_visualization(){
        int i = 0;
        for (Alumno al: contenido
             ) {
            String res = ""+i+") ";
            if(al == null){
                res = res + "null.";
                System.out.println(res);
            }else{
                System.out.println(res+al.get_visualization());
            }
            i++;
        }
    }
    public Alumno[] check_backup(Alumno[] backup){
        if(backup.length == size)return backup;
        Alumno[] new_backup = new Alumno[size];
        int i = 0;
        for (Alumno al: backup
             ) {
            new_backup[i] = al;
            i++;
        }
        return new_backup;
    }
    private  static  int ren =0;
    public void visit_preorder(Alumno[] vessel, int node){
        if (contenido[node] == null)
            return;
        vessel[ren] = contenido[node];
        ren++;
        visit_preorder(vessel, left(node));

        visit_preorder(vessel, right(node));

    }
    public void visit_postorder(Alumno[] vessel, int node){
        if (contenido[node] == null)
            return;

        visit_postorder(vessel, left(node));

        visit_postorder(vessel, right(node));

        vessel[ren] = contenido[node];
        ren++;
    }
    public void visit_inorden(Alumno[] vessel, int node){
        if (contenido[node] == null)
            return;

        visit_inorden(vessel, left(node));

        vessel[ren] = contenido[node];
        ren++;

        visit_inorden(vessel, right(node));

    }
    public Alumno[] get_preorder(){
        Alumno[] res = new Alumno[filled_nodes];
        ren = 0;
        visit_preorder(res,0);
        return  res;
    }
    public Alumno[] get_postorder(){
        Alumno[] res = new Alumno[filled_nodes];
        ren = 0;
        visit_postorder(res,0);
        return res;
    }
    public Alumno[] get_inorden(){
        Alumno[] res = new Alumno[filled_nodes];
        ren = 0;
        visit_inorden(res,0);
        return res;
    }
}
