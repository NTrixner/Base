import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { InternalComponent } from './components/internal/internal.component';
import { LoginPageComponent } from './components/login-page/login-page.component';

const routes: Routes = [
  {path: '', component: LoginPageComponent},
  {path: 'internal', component: InternalComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
