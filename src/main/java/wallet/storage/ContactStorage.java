package wallet.storage;

import wallet.model.contact.Contact;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

public class ContactStorage extends Storage<Contact> {
    public static final String DEFAULT_STORAGE_FILEPATH_CONTACT = "./data/contact.txt";
    public static final String MESSAGE_ERROR_MISSING_FILE = "No saved contacts found.";
    public static final String MESSAGE_ERROR_FILE_EOF = "End of file.";
    public static final String MESSAGE_ERROR_WRITE_FILE = "IOException: ";

    /**
     * Loads the contacts from contact.txt into a temporary ArrayList of Contact objects.
     *
     * @return The ArrayList of Contact objects.
     */
    @Override
    public ArrayList<Contact> loadFile() {
        ArrayList<Contact> contactList = new ArrayList<>();

        try {
            RandomAccessFile raf = new RandomAccessFile(DEFAULT_STORAGE_FILEPATH_CONTACT, "rws");
            String str;
            while (raf.getFilePointer() != raf.length()) {
                str = raf.readLine();
                String[] data = str.split(",", -1);
                Contact contact = null;

                if (data.length == 4) {
                    //@@author Xdecosee
                    if (data[2].equals("")) {
                        data[2] = null;
                    }
                    if (data[3].equals("")) {
                        data[3] = null;
                    }
                    //@@author
                    contact = new Contact(data[1], data[2], data[3]);
                }

                if (contact != null) {
                    contact.setId(Integer.parseInt(data[0]));
                    contactList.add(contact);
                }
            }
            raf.close();
        } catch (FileNotFoundException e) {
            System.out.println(MESSAGE_ERROR_MISSING_FILE);
        } catch (IOException e) {
            System.out.println(MESSAGE_ERROR_FILE_EOF);
        }
        return contactList;
    }

    /**
     * Writes the contact list into contacts.txt.
     *
     * @param contactList The list of contacts.
     */
    @Override
    public void writeListToFile(ArrayList<Contact> contactList) {
        try {
            RandomAccessFile raf = new RandomAccessFile(DEFAULT_STORAGE_FILEPATH_CONTACT, "rws");
            raf.setLength(0);

            for (Contact contact : contactList) {
                if (raf.getFilePointer() != 0) {
                    raf.writeBytes("\r\n");
                }
                raf.writeBytes(contact.writeToFile());
            }
            raf.close();
        } catch (IOException e) {
            System.out.println(MESSAGE_ERROR_WRITE_FILE + e.getMessage());
        }
    }
}
