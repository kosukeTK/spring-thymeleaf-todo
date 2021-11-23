  /*const getUserTodoInfo = () => {
	  document.forms["email_select"].email.value = document.getElementById("email").value;
  	document.forms["email_select"].submit();	
  }*/
  $('#email').on('change', function() {
		console.log($('#email_select').val())
		$('#email_select').val() = $('#email').val();
		$('#email_select').submit();
	})
  
  const getTaskSort = () => {
	  document.forms["sort_form"].sort.value	= document.getElementById("sort").value;
	  document.forms["sort_form"].email.value	= document.getElementById("td_email").textContent;
  	document.forms["sort_form"].submit();
  }
  
	const fileInfo = () => {
	        var text = $('[name="task_checkbox"]:checked').val();
	        console.log(text)
	        var text1 = $('[name="task_checkbox"]:checked');
	        console.log(text1)
	}

// 		let task = [];
// 		let taskList = document.getElementById('taskList');
// 		let td = taskList.querySelectorAll('td')
// 		td.forEach(c => task.push(c.textContent));
// 		console.log(task);
  
  var userModal = document.getElementById('userModal')
  userModal.addEventListener('show.bs.modal', function (event) {
    // Button that triggered the modal
    var button = event.relatedTarget
    // Extract info from data-bs-* attributes
    var modify		= button.getAttribute('data-bs-modify')
    var id				= button.getAttribute('data-bs-id')
    var username	= button.getAttribute('data-bs-username')
    var email			= button.getAttribute('data-bs-email')
    var role			= button.getAttribute('data-bs-role')
    var enabled		= button.getAttribute('data-bs-enabled')
    var locked		= button.getAttribute('data-bs-locked')
    // Update the modal's content.
    userModal.querySelector('#id').value				= id
    userModal.querySelector('#user_name').value	= username
    userModal.querySelector('#email').value			= email
    userModal.querySelector('#role').value			= role
    userModal.querySelector('#enabled').value		= enabled
    userModal.querySelector('#locked').value		= locked
    
    if(modify == "insert") {
    	userModal.querySelector('.modal-title').textContent = "ユーザー情報新規作成"
    	userModal.querySelector('#submit_btn').value	= "作成"
    	userModal.querySelector('#user_form').action = "/admin/insertUser"
    } else {
    	userModal.querySelector('.modal-title').textContent = "ユーザー情報更新"
    	userModal.querySelector('#submit_btn').value	= "更新"
    	userModal.querySelector('#user_form').action = "/admin/editUser"
    }
  })
  
  var taskModal = document.getElementById('taskModal')
  taskModal.addEventListener('show.bs.modal', function (event) {
    // Button that triggered the modal
    var button = event.relatedTarget
    // Extract info from data-bs-* attributes
    var modify		= button.getAttribute('data-bs-modify')
    var id				= button.getAttribute('data-bs-id')
    var taskName	= button.getAttribute('data-bs-taskName')
    var category	= button.getAttribute('data-bs-category')
    var description	= button.getAttribute('data-bs-description')
    var content		= button.getAttribute('data-bs-content')
    var status		= button.getAttribute('data-bs-status')
    var taskDate	= button.getAttribute('data-bs-taskDate')
    var userId		= button.getAttribute('data-bs-userId')
    // Update the modal's content.
    taskModal.querySelector('#id').value				= id
    taskModal.querySelector('#taskName').value	= taskName
    taskModal.querySelector('#category').value	= category
    taskModal.querySelector('#description').value	= description
    taskModal.querySelector('#content').value		= content
    taskModal.querySelector('#status').value		= status
    taskModal.querySelector('#taskDate').value	= taskDate
    taskModal.querySelector('#userId').value		= userId
    
    if(modify == "insert") {
    	taskModal.querySelector('.modal-title').textContent = "タスク情報新規作成"
    	taskModal.querySelector('#submit_btn').value	= "作成"
    	taskModal.querySelector('#task_form').action = "/admin/insertTask"
    } else {
    	taskModal.querySelector('.modal-title').textContent = "タスク情報更新"
    	taskModal.querySelector('#submit_btn').value	= "更新"
    	taskModal.querySelector('#task_form').action = "/admin/editTask"
    }
  })