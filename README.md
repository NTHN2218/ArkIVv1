# ArkIVv1

   ArkIVv1 - A simple To-Do List application with GUI (Swing)
  
   Salient Features:
  
   1.   Task Management    
      - Allows users to create, edit, delete, and mark tasks as done/undone.  
      - Supports both main tasks and subtasks (visually indented with a different background).  
  
   2.   Task Persistence    
      - Tasks are automatically saved to a local file (`data_1.txt`).  
      - On restart, saved tasks are loaded back into the application.  
  
   3.   User-Friendly Interface    
      - Clean layout using `BorderLayout` and `BoxLayout`.  
      - Scrollable panel for handling many tasks.  
      - Input field at the bottom for quick task entry (press   Enter   to add).  
      - Larger font size for readability.  
  
   4.   Task Controls    
      - Checkbox to mark tasks as done (strikethrough + gray text applied).  
      - Edit button to update task text.  
      - Delete button to remove tasks.  
      - Create Subtask button (only for main tasks) to add subtasks directly.  
  
   5.   Consistent Design    
      - Each task has a fixed size for neat appearance.  
      - Main tasks have white background, subtasks are shaded gray with indentation.  
      - Uses borders for separation and hierarchy visualization.  
  
   6.   Error Handling    
      - handling of file read/write errors with dialog messages.  
  
   7.   Data Storage Format   (inside `data_1.txt`)  
      Each task is stored on a new line in the format:  

   Overall:  
   This version establishes the foundation for a structured, persistent, and visually organized Archiving application.
