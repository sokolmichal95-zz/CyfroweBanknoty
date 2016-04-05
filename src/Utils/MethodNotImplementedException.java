/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;

/**
 *
 * @author msokol
 */
public class MethodNotImplementedException extends Exception {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * Creates a new instance of <code>ClassNotImplementedException</code>
     * without detail message.
     */
    public MethodNotImplementedException() {
    }

    /**
     * Constructs an instance of <code>ClassNotImplementedException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public MethodNotImplementedException(String msg) {
        super(msg);
    }
}
